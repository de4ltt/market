package ru.market.pricing_service.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.hr_service.model.dto.EmployeeDto;
import ru.market.hr_service.model.entity.Employee;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.hr_service.service.EmployeeService;
import ru.market.inventory_service.model.dto.ProductDto;
import ru.market.inventory_service.model.dto.StockOperationDto;
import ru.market.inventory_service.model.dto.StorageLocationDto;
import ru.market.inventory_service.model.entity.Product;
import ru.market.inventory_service.repository.ProductRepository;
import ru.market.inventory_service.service.ProductService;
import ru.market.inventory_service.service.StockOperationService;
import ru.market.inventory_service.service.StorageLocationService;
import ru.market.pricing_service.model.dto.CouponDto;
import ru.market.pricing_service.model.dto.PriceOrderResponse;
import ru.market.pricing_service.model.dto.PriceTag;
import ru.market.pricing_service.model.dto.StopListItemDto;
import ru.market.pricing_service.model.entity.Check;
import ru.market.pricing_service.model.entity.Discount;
import ru.market.pricing_service.model.entity.PriceList;
import ru.market.pricing_service.model.entity.ProductPriceInCheck;
import ru.market.pricing_service.repository.CheckRepository;
import ru.market.pricing_service.repository.DiscountRepository;
import ru.market.pricing_service.repository.PriceListRepository;
import ru.market.pricing_service.repository.ProductPriceInCheckRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.*;


@Service
@Transactional
@Slf4j
public class PricingService {

    private static final String REGULAR_TYPE = "регулярная";
    private static final String MARKDOWN_AUTO_TYPE = "уценочная_авто";
    private static final String ADD_OPERATION_TYPE = "add";
    private static final String REMOVE_OPERATION_TYPE = "remove";
    private static final BigDecimal COUPON_DISCOUNT = new BigDecimal("0.30");
    private static final long NEAR_EXPIRY_DAYS = 5L;
    private static final int AUTO_MARKDOWN_THRESHOLD_STOCK = 50;
    private static final long AUTO_MARKDOWN_THRESHOLD_SALES = 20L;
    private static final BigDecimal AUTO_MARKDOWN_COEFFICIENT = new BigDecimal("0.75");
    private static final long SALES_PERIOD_DAYS = 7L;

    // key -> (maxMarkupPercent, maxDailyChangePercent)
    private final Map<String, Pair<BigDecimal, BigDecimal>> restrictedKeywordsToRestrictions = Map.of(
            "детск", new Pair<>(new BigDecimal("15"), new BigDecimal("5")),
            "молочн", new Pair<>(new BigDecimal("15"), new BigDecimal("5")),
            "хлеб", new Pair<>(new BigDecimal("15"), new BigDecimal("5"))
    );

    // базовые значения: (maxMarkupPercent, maxDailyChangePercent)
    private final Pair<BigDecimal, BigDecimal> defaultRestriction = new Pair<>(new BigDecimal("1000"), new BigDecimal("90"));

    private final ProductService productService;
    private final StockOperationService stockOperationService;
    private final StorageLocationService storageLocationService;
    private final EmployeeService employeeService;
    private final PriceListRepository priceListRepository;
    private final ProductPriceInCheckRepository productPriceInCheckRepository;
    private final CheckRepository checkRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final EmployeeRepository employeeRepository; // предполагается в проекте

    @Autowired
    public PricingService(
        ProductService productService,
        StockOperationService stockOperationService,
        StorageLocationService storageLocationService,
        EmployeeService employeeService,
        PriceListRepository priceListRepository,
        ProductPriceInCheckRepository productPriceInCheckRepository,
        CheckRepository checkRepository,
        ProductRepository productRepository,
        DiscountRepository discountRepository,
        EmployeeRepository employeeRepository
    ) {
        this.productService = productService;
        this.stockOperationService = stockOperationService;
        this.storageLocationService = storageLocationService;
        this.employeeService = employeeService;
        this.priceListRepository = priceListRepository;
        this.productPriceInCheckRepository = productPriceInCheckRepository;
        this.checkRepository = checkRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Формирование приказа директором (08:00-09:00 локального времени).
     * Возвращает объект с изменёнными товарами, стоп-листом, ценниками и купонами.
     */
    public PriceOrderResponse formPrices(int directorId) {
        EmployeeDto directorDto = employeeService.getEmployee(directorId);
        if (directorDto == null || !"директор".equalsIgnoreCase(directorDto.getRole())) {
            throw new RuntimeException("Only director can initiate price order");
        }

        ZoneId storeZone = ZoneId.systemDefault();
        LocalTime nowLocal = LocalTime.now(storeZone);
        if (nowLocal.isBefore(LocalTime.of(8, 0)) || nowLocal.isAfter(LocalTime.of(9, 0))) {
            throw new RuntimeException("Price order can only be formed between 08:00 and 09:00 local time");
        }

        LocalDate today = LocalDate.now(storeZone);
        LocalDate yesterday = today.minusDays(1);

        // Получаем/создаём Check для сегодняшнего приказа (связан с директором)
        Check currentCheck = getOrCreateCheckForDate(today, directorId);

        // Получаем минимальные цены за вчерашний день
        Map<Integer, BigDecimal> previousMinPrices = getMinPricesForDate(yesterday);

        List<StockOperationDto> allOperations = stockOperationService.getAll();
        Map<Integer, String> locationTypeMap = storageLocationService.getAll().stream()
                .filter(loc -> loc.getStorageLocationId() != null)
                .collect(Collectors.toMap(StorageLocationDto::getStorageLocationId, StorageLocationDto::getType));

        // Автоуценки: создают новые записи в currentCheck или возвращают список заблокированных продуктов
        List<StopListItemDto> blockedFromAutoMarkdown = generateAutoMarkdowns(today, allOperations, locationTypeMap, currentCheck);

        List<ProductDto> updatedProducts = new ArrayList<>();
        List<StopListItemDto> stopList = new ArrayList<>();
        stopList.addAll(blockedFromAutoMarkdown);
        List<PriceTag> priceTags = new ArrayList<>();
        List<CouponDto> coupons = new ArrayList<>();

        List<ProductDto> productDtos = productService.getAll();

        for (ProductDto productDto : productDtos) {
            Integer productId = productDto.getProductId();
            if (productId == null) continue;

            Product product = productRepository.getReferenceById(productId);

            List<ProductPriceInCheck> currentRecords = productPriceInCheckRepository.findByCheckAndProduct(currentCheck, product);

            List<ProductPriceInCheck> activeRecords = currentRecords.stream()
                    .filter(it -> !it.getPriceList().getEffectiveDate().isAfter(today) &&
                            !it.getPriceList().getEndDate().isBefore(today))
                    .toList();

            if (activeRecords.isEmpty()) continue;

            // 1) выбираем минимальную финальную цену среди всех активных прайс-листов
            ProductPriceInCheck effectiveRecord = activeRecords.stream()
                    .min(Comparator.comparing(ProductPriceInCheck::getFinalPrice))
                    .orElse(null);

            if (effectiveRecord == null) continue;

            BigDecimal effectivePrice = effectiveRecord.getFinalPrice();
            String effectiveType = effectiveRecord.getPriceType();

            BigDecimal previousPrice = previousMinPrices.getOrDefault(productId, effectivePrice);

            if (effectivePrice.compareTo(previousPrice) != 0) {
                updatedProducts.add(productDto);
            }

            Pair<BigDecimal, BigDecimal> restrictions = getRestrictionsForProduct(productDto);
            BigDecimal maxMarkupPct = restrictions.getLeft();  // проценты
            BigDecimal maxChangePct = restrictions.getRight(); // проценты

            BigDecimal inputPrice = effectiveRecord.getInputPrice() == null ? BigDecimal.ZERO : effectiveRecord.getInputPrice();
            BigDecimal maxAllowedMarkupPrice = inputPrice.multiply(BigDecimal.ONE.add(maxMarkupPct.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)));

            boolean isDecrease = effectivePrice.compareTo(previousPrice) < 0;
            boolean isAutoMarkdown = isDecrease && MARKDOWN_AUTO_TYPE.equalsIgnoreCase(effectiveType);

            // процент изменения относительно предыдущей цены (0..100)
            BigDecimal changePercent = calculateChangePercent(effectivePrice, previousPrice);
            boolean changeExceeds = changePercent.compareTo(maxChangePct) > 0;

            // если превышен дневной лимит и это не автоуценка, ограничиваем изменение относительно previousPrice
            if (changeExceeds && !isAutoMarkdown) {
                boolean increase = effectivePrice.compareTo(previousPrice) > 0;
                BigDecimal cappedDelta = previousPrice.multiply(maxChangePct).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
                BigDecimal adjustedPrice = increase ? previousPrice.add(cappedDelta) : previousPrice.subtract(cappedDelta);
                adjustedPrice = adjustedPrice.setScale(2, RoundingMode.HALF_UP);

                // сохраняем скорректированную запись в currentCheck (чтобы официально надеть цену)
                ProductPriceInCheck adjustedRecord = new ProductPriceInCheck();
                adjustedRecord.setProduct(product);
                adjustedRecord.setCheck(currentCheck);
                adjustedRecord.setPriceList(effectiveRecord.getPriceList());
                adjustedRecord.setDiscount(effectiveRecord.getDiscount());
                adjustedRecord.setInputPrice(inputPrice);
                adjustedRecord.setFinalPrice(adjustedPrice);
                adjustedRecord.setPriceType(effectiveType + "_capped");

                productPriceInCheckRepository.save(adjustedRecord);

                effectivePrice = adjustedPrice;
                effectiveRecord = adjustedRecord;
                changePercent = calculateChangePercent(effectivePrice, previousPrice);
            }

            // Проверка критического ограничения: верхний лимит наценки — если нарушен, идёт в стоп-лист
            boolean markupOk = inputPrice.compareTo(BigDecimal.ZERO) == 0
                    ? effectivePrice.compareTo(BigDecimal.ZERO) == 0
                    : effectivePrice.compareTo(maxAllowedMarkupPrice) <= 0;

            if (!markupOk) {
                stopList.add(new StopListItemDto(productId, "Превышение лимита наценки", LocalDate.now()));
                continue;
            }

            // Для печати ценника: регулярная цена должна быть указана — выбираем максимальную среди регулярных
            BigDecimal regularPrice = activeRecords.stream()
                    .filter(it -> REGULAR_TYPE.equalsIgnoreCase(it.getPriceType()))
                    .map(ProductPriceInCheck::getFinalPrice)
                    .max(Comparator.naturalOrder())
                    .orElse(effectivePrice);

            String tagType = REGULAR_TYPE.equalsIgnoreCase(effectiveType) ? "белый" :
                    effectiveType != null && effectiveType.toLowerCase().contains("акцион") ? "акционный" : "желтый";

            String promoDescription = extractPromoDescription(effectiveType);

            priceTags.add(new PriceTag(productDto.getName(), effectivePrice, tagType, regularPrice, promoDescription));

            if (effectiveType != null && effectiveType.toLowerCase().contains("уцен")) {
                LocalDate weekAgo = LocalDate.now().minusDays(SALES_PERIOD_DAYS);
                Pair<Integer, Integer> stock = calculateStockByLocation(allOperations, productId, locationTypeMap);
                long soldLastWeek = calculateSoldUnitsInPeriod(allOperations, productId, weekAgo, LocalDate.now(), locationTypeMap);
                log.info("Передача данных по уценке в ГК: productId={}, type={}, hallStock={}, warehouseStock={}, totalStock={}, salesLastWeek={}",
                        productId, effectiveType, stock.getLeft(), stock.getRight(), stock.getLeft() + stock.getRight(), soldLastWeek);
            }
        }

        coupons.addAll(calculateNearExpiryCoupons(LocalDate.now(), allOperations));

        if (!stopList.isEmpty()) {
            log.info("Передача стоп-листа в ГК: {}", stopList);
        }

        return new PriceOrderResponse(updatedProducts, stopList, priceTags, coupons);
    }

    // ====================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ======================

    /**
     * Вычисляет процент изменения цены между новой и старой ценой.
     */
    private BigDecimal calculateChangePercent(BigDecimal newPrice, BigDecimal oldPrice) {
        if (oldPrice == null || oldPrice.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.valueOf(100);
        BigDecimal diff = newPrice.subtract(oldPrice).abs();
        return diff.multiply(BigDecimal.valueOf(100)).divide(oldPrice, 10, RoundingMode.HALF_UP);
    }

    /**
     * Извлекает описание акции из типа цены (например, для "1+1").
     */
    private String extractPromoDescription(String type) {
        if (type == null) return null;
        Pattern p1 = Pattern.compile("акция_(\\d+)\\+1", Pattern.CASE_INSENSITIVE);
        Matcher m1 = p1.matcher(type);
        if (m1.find()) {
            return "Купи " + m1.group(1) + " — получи 1 бесплатно";
        }
        Pattern p2 = Pattern.compile("акция_(\\d+)\\+(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher m2 = p2.matcher(type);
        if (m2.find()) {
            return "Купи " + m2.group(1) + " — получи " + m2.group(2) + " бесплатно";
        }
        return null;
    }

    /**
     * Получает минимальные цены для товаров на указанную дату.
     */
    private Map<Integer, BigDecimal> getMinPricesForDate(LocalDate date) {
        Check check = findCheckByDate(date);
        if (check == null) return Collections.emptyMap();

        return productPriceInCheckRepository.findByCheck(check).stream()
                .filter(it -> !it.getPriceList().getEffectiveDate().isAfter(date) &&
                        !it.getPriceList().getEndDate().isBefore(date))
                .collect(Collectors.groupingBy(it -> it.getProduct().getProductId(),
                        Collectors.mapping(ProductPriceInCheck::getFinalPrice, Collectors.minBy(Comparator.naturalOrder()))))
                .entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    /**
     * Генерирует автоуценки для товаров, возвращает заблокированные из-за нарушений.
     */
    private List<StopListItemDto> generateAutoMarkdowns(LocalDate today,
                                                        List<StockOperationDto> allOperations,
                                                        Map<Integer, String> locationTypeMap,
                                                        Check currentCheck) {
        List<StopListItemDto> blocked = new ArrayList<>();
        List<ProductDto> productDtos = productService.getAll();
        LocalDate weekAgo = today.minusDays(SALES_PERIOD_DAYS);
        Discount noDiscount = getOrCreateNoDiscount();

        for (ProductDto productDto : productDtos) {
            Integer productId = productDto.getProductId();
            if (productId == null) continue;

            Product product = productRepository.getReferenceById(productId);

            Pair<Integer, Integer> stock = calculateStockByLocation(allOperations, productId, locationTypeMap);
            int totalStock = stock.getLeft() + stock.getRight();
            long soldLastWeek = calculateSoldUnitsInPeriod(allOperations, productId, weekAgo, today, locationTypeMap);

            if (totalStock > AUTO_MARKDOWN_THRESHOLD_STOCK && soldLastWeek < AUTO_MARKDOWN_THRESHOLD_SALES) {

                List<ProductPriceInCheck> currentRecords = productPriceInCheckRepository.findByCheckAndProduct(currentCheck, product);

                ProductPriceInCheck regularRecord = currentRecords.stream()
                        .filter(it -> REGULAR_TYPE.equalsIgnoreCase(it.getPriceType()))
                        .findFirst()
                        .orElse(null);

                if (regularRecord == null) continue;

                BigDecimal inputPrice = regularRecord.getInputPrice() == null ? BigDecimal.ZERO : regularRecord.getInputPrice();
                BigDecimal markdownPrice = regularRecord.getFinalPrice().multiply(AUTO_MARKDOWN_COEFFICIENT).setScale(2, RoundingMode.HALF_UP);

                Pair<BigDecimal, BigDecimal> restrictions = getRestrictionsForProduct(productDto);
                BigDecimal maxMarkupPct = restrictions.getLeft();
                BigDecimal maxAllowedMarkupPrice = inputPrice.multiply(BigDecimal.ONE.add(maxMarkupPct.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)));

                boolean markupOk = inputPrice.compareTo(BigDecimal.ZERO) == 0
                        ? markdownPrice.compareTo(BigDecimal.ZERO) == 0
                        : markdownPrice.compareTo(maxAllowedMarkupPrice) <= 0;

                log.info("Автоуценка (product={}): hall={}, warehouse={}, total={}, salesLastWeek={} {}",
                        productId, stock.getLeft(), stock.getRight(), totalStock, soldLastWeek, (markupOk ? "" : "(ЗАБЛОКИРОВАНО)"));

                if (markupOk) {
                    PriceList markdownPl = getOrCreatePriceList(MARKDOWN_AUTO_TYPE, today, today.plusDays(7));

                    ProductPriceInCheck markdownRecord = new ProductPriceInCheck();
                    markdownRecord.setProduct(product);
                    markdownRecord.setCheck(currentCheck);
                    markdownRecord.setPriceList(markdownPl);
                    markdownRecord.setDiscount(noDiscount);
                    markdownRecord.setInputPrice(inputPrice);
                    markdownRecord.setFinalPrice(markdownPrice);
                    markdownRecord.setPriceType(MARKDOWN_AUTO_TYPE);

                    productPriceInCheckRepository.save(markdownRecord);
                } else {
                    blocked.add(new StopListItemDto(productId, "Превышение лимита наценки при автоуценке", today));
                }
            }
        }
        return blocked;
    }

    /**
     * Получает или создаёт прайс-лист по типу и датам.
     */
    private PriceList getOrCreatePriceList(String type, LocalDate effective, LocalDate end) {
        // используем provided repository метод для фильтрации по датам
        List<PriceList> candidates = priceListRepository.findByEffectiveDateLessThanEqualAndEndDateGreaterThanEqual(effective, effective);
        Optional<PriceList> existing = candidates.stream()
                .filter(pl -> pl.getType() != null && pl.getType().equalsIgnoreCase(type))
                .findFirst();

        if (existing.isPresent()) return existing.get();

        PriceList newPl = new PriceList();
        newPl.setType(type);
        newPl.setEffectiveDate(effective);
        newPl.setEndDate(end == null ? effective.plusYears(10) : end);
        return priceListRepository.save(newPl);
    }

    /**
     * Получает или создаёт скидку "без скидки".
     */
    private Discount getOrCreateNoDiscount() {
        // если в репозитории нет специализированного метода — ищем через findAll()
        Optional<Discount> found = discountRepository.findAll().stream()
                .filter(d -> d.getDiscountType() != null &&
                        (d.getDiscountType().equalsIgnoreCase("none") ||
                                d.getDiscountType().equalsIgnoreCase("без скидки") ||
                                d.getDiscountType().equalsIgnoreCase("no_discount")))
                .findFirst();

        if (found.isPresent()) return found.get();

        Discount d = new Discount();
        d.setDiscountType("none");
        d.setDiscountSize(BigDecimal.ZERO);
        return discountRepository.save(d);
    }

    /**
     * Находит чек по указанной дате.
     */
    private Check findCheckByDate(LocalDate date) {
        // Если вы добавите в CheckRepository метод findByDate(LocalDate date) — замените на него.
        return checkRepository.findAll().stream()
                .filter(c -> date.equals(c.getDate()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Получает или создаёт чек для указанной даты, связанный с директором.
     */
    private Check getOrCreateCheckForDate(LocalDate date, int directorId) {
        Check existing = findCheckByDate(date);
        if (existing != null) return existing;

        Employee director = employeeRepository.getReferenceById(directorId);

        Check newCheck = new Check();
        newCheck.setEmployee(director);
        newCheck.setDate(date);
        return checkRepository.save(newCheck);
    }

    /**
     * Вычисляет остатки товара в зале и на складе.
     */
    private Pair<Integer, Integer> calculateStockByLocation(List<StockOperationDto> allOperations, int productId, Map<Integer, String> locationTypeMap) {
        int hall = 0;
        int warehouse = 0;
        for (StockOperationDto op : allOperations) {
            if (op.getProductId() == productId) {
                int amount = ADD_OPERATION_TYPE.equalsIgnoreCase(op.getOperationType()) ? op.getQuantity() : -op.getQuantity();
                String locType = locationTypeMap.getOrDefault(op.getStorageLocationId(), "");
                if ("trading_hall".equalsIgnoreCase(locType) || "hall".equalsIgnoreCase(locType)) {
                    hall += amount;
                } else {
                    warehouse += amount;
                }
            }
        }
        return new Pair<>(Math.max(hall, 0), Math.max(warehouse, 0));
    }

    /**
     * Вычисляет количество проданных единиц товара за период.
     */
    private long calculateSoldUnitsInPeriod(List<StockOperationDto> allOperations, int productId, LocalDate start, LocalDate end, Map<Integer, String> locationTypeMap) {
        return allOperations.stream()
                .filter(op -> op.getProductId() == productId &&
                        REMOVE_OPERATION_TYPE.equalsIgnoreCase(op.getOperationType()) &&
                        !op.getTimestamp().isBefore(start) &&
                        !op.getTimestamp().isAfter(end) &&
                        "trading_hall".equalsIgnoreCase(locationTypeMap.getOrDefault(op.getStorageLocationId(), "")))
                .mapToLong(StockOperationDto::getQuantity)
                .sum();
    }

    /**
     * Получает ограничения (наценка и изменение цены) для товара по его названию.
     */
    private Pair<BigDecimal, BigDecimal> getRestrictionsForProduct(ProductDto productDto) {
        String nameLower = productDto.getName() == null ? "" : productDto.getName().toLowerCase();
        return restrictedKeywordsToRestrictions.entrySet().stream()
                .filter(entry -> nameLower.contains(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(defaultRestriction);
    }

    /**
     * Генерирует купоны для товаров с приближающимся сроком годности.
     */
    private List<CouponDto> calculateNearExpiryCoupons(LocalDate today, List<StockOperationDto> allOperations) {
        List<CouponDto> coupons = new ArrayList<>();

        List<StockOperationDto> candidateAdds = allOperations.stream()
                .filter(it -> ADD_OPERATION_TYPE.equalsIgnoreCase(it.getOperationType()) &&
                        it.getExpiryDate() != null &&
                        it.getExpiryDate().isAfter(today) &&
                        !it.getExpiryDate().isAfter(today.plusDays(NEAR_EXPIRY_DAYS)) &&
                        it.getQuantity() > 0)
                .toList();

        if (candidateAdds.isEmpty()) return coupons;

        Set<Integer> products = candidateAdds.stream()
                .map(StockOperationDto::getProductId)
                .collect(Collectors.toSet());

        for (int productId : products) {
            List<StockOperationDto> productOps = allOperations.stream()
                    .filter(it -> it.getProductId() == productId)
                    .sorted(Comparator.comparing(StockOperationDto::getTimestamp))
                    .toList();

            List<Batch> batches = new ArrayList<>();

            for (StockOperationDto op : productOps) {
                if (ADD_OPERATION_TYPE.equalsIgnoreCase(op.getOperationType())) {
                    int opId = op.getStockOperationId() != null ? op.getStockOperationId() : 0;
                    batches.add(new Batch(opId, op.getExpiryDate(), op.getQuantity()));
                } else if (REMOVE_OPERATION_TYPE.equalsIgnoreCase(op.getOperationType())) {
                    int toRemove = op.getQuantity();
                    int idx = 0;
                    while (toRemove > 0 && idx < batches.size()) {
                        Batch batch = batches.get(idx);
                        if (batch.remaining > 0) {
                            int reduce = Math.min(toRemove, batch.remaining);
                            batch.remaining -= reduce;
                            toRemove -= reduce;
                        }
                        idx++;
                    }
                }
            }

            for (Batch batch : batches) {
                if (batch.id != 0 && batch.remaining > 0 &&
                        batch.expiryDate != null &&
                        batch.expiryDate.isAfter(today) &&
                        !batch.expiryDate.isAfter(today.plusDays(NEAR_EXPIRY_DAYS))) {

                    BigDecimal percent = COUPON_DISCOUNT.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.UNNECESSARY);
                    coupons.add(new CouponDto(batch.id, batch.expiryDate, COUPON_DISCOUNT,
                            "Близкий срок годности (осталось " + batch.remaining + " шт.) — скидка " + percent + "%"));
                }
            }
        }

        return coupons;
    }

    /**
     * Внутренний класс для представления партии товара с истекающим сроком.
     */
    private static class Batch {
        int id;
        LocalDate expiryDate;
        int remaining;

        Batch(int id, LocalDate expiryDate, int remaining) {
            this.id = id;
            this.expiryDate = expiryDate;
            this.remaining = remaining;
        }
    }

    /**
     * Внутренний класс для представления пары значений (например, наценка и изменение цены).
     */
    private static class Pair<L, R> {
        private final L left;
        private final R right;

        Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        L getLeft() { return left; }
        R getRight() { return right; }
    }
}
