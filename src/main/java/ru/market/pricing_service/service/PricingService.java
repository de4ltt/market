package ru.market.pricing_service.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.hr_service.model.dto.EmployeeDto;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.*;

@Service
@Transactional
public class PricingService {

    private static final int CURRENT_PRICE_CHECK_ID = 0;
    private static final int NO_DISCOUNT_ID = 0;
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

    private final Map<String, Pair<BigDecimal, BigDecimal>> restrictedKeywordsToRestrictions = Map.of(
            "детск", new Pair<>(new BigDecimal("15"), new BigDecimal("5")),
            "молочн", new Pair<>(new BigDecimal("15"), new BigDecimal("5")),
            "хлеб", new Pair<>(new BigDecimal("15"), new BigDecimal("5"))
    );

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

    @Autowired
    public PricingService(ProductService productService,
                          StockOperationService stockOperationService,
                          StorageLocationService storageLocationService,
                          EmployeeService employeeService,
                          PriceListRepository priceListRepository,
                          ProductPriceInCheckRepository productPriceInCheckRepository,
                          CheckRepository checkRepository,
                          ProductRepository productRepository,
                          DiscountRepository discountRepository) {
        this.productService = productService;
        this.stockOperationService = stockOperationService;
        this.storageLocationService = storageLocationService;
        this.employeeService = employeeService;
        this.priceListRepository = priceListRepository;
        this.productPriceInCheckRepository = productPriceInCheckRepository;
        this.checkRepository = checkRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    public PriceOrderResponse formPrices(int directorId) {
        EmployeeDto director = employeeService.getEmployee(directorId);
        if (!"DIRECTOR".equalsIgnoreCase(director.getRole())) {
            throw new RuntimeException("Only director can initiate price order");
        }

        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(8, 0)) || now.isAfter(LocalTime.of(9, 0))) {
            throw new RuntimeException("Price order can only be formed between 08:00 and 09:00");
        }

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        Map<Integer, BigDecimal> previousMinPrices = getCurrentMinPrices(yesterday);

        List<StockOperationDto> allOperations = stockOperationService.getAll();
        Map<Integer, String> locationTypeMap = storageLocationService.getAll().stream()
                .filter(loc -> loc.getStorageLocationId() != null)
                .collect(Collectors.toMap(StorageLocationDto::getStorageLocationId, StorageLocationDto::getType));

        // Автоуценка теперь возвращает список заблокированных товаров (нарушение наценки)
        List<StopListItemDto> blockedFromAutoMarkdown = generateAutoMarkdowns(today, allOperations, locationTypeMap);

        List<ProductDto> updatedProducts = new ArrayList<>();
        List<StopListItemDto> stopList = new ArrayList<>();
        stopList.addAll(blockedFromAutoMarkdown); // Добавляем заблокированные автоуценки в общий стоп-лист
        List<PriceTag> priceTags = new ArrayList<>();
        List<CouponDto> coupons = new ArrayList<>();

        List<ProductDto> productDtos = productService.getAll();

        Check currentCheck = checkRepository.getReferenceById(CURRENT_PRICE_CHECK_ID);

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

            ProductPriceInCheck effectiveRecord = activeRecords.stream()
                    .min(Comparator.comparing(ProductPriceInCheck::getFinalPrice))
                    .orElse(null);

            if (effectiveRecord == null) continue;

            BigDecimal effectivePrice = effectiveRecord.getFinalPrice();
            String effectiveType = effectiveRecord.getPriceType();

            BigDecimal previousPrice = previousMinPrices.getOrDefault(productId, effectivePrice);

            if (!effectivePrice.equals(previousPrice)) {
                updatedProducts.add(productDto);
            }

            // Ограничения (наценка + изменение цены, кроме автоуценки при снижении)
            Pair<BigDecimal, BigDecimal> restrictions = getRestrictionsForProduct(productDto);
            BigDecimal maxMarkupPct = restrictions.getLeft();
            BigDecimal maxChangePct = restrictions.getRight();

            BigDecimal inputPrice = effectiveRecord.getInputPrice();
            BigDecimal maxAllowedMarkupPrice = inputPrice.multiply(BigDecimal.ONE.add(maxMarkupPct.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)));

            boolean markupOk = effectivePrice.compareTo(maxAllowedMarkupPrice) <= 0;

            boolean isDecrease = effectivePrice.compareTo(previousPrice) < 0;
            boolean isAutoMarkdown = isDecrease && MARKDOWN_AUTO_TYPE.equals(effectiveType);

            boolean changeOk = isAutoMarkdown ||
                    calculateChangePercent(effectivePrice, previousPrice)
                            .compareTo(maxChangePct.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)) <= 0;

            if (!markupOk || !changeOk) {
                String reason = !markupOk ? "Превышение лимита наценки" : "Слишком сильное изменение цены";
                stopList.add(new StopListItemDto(productId, reason, today));
            }

            // Регулярная цена для ценника
            BigDecimal regularPrice = activeRecords.stream()
                    .filter(it -> REGULAR_TYPE.equals(it.getPriceType()))
                    .findFirst()
                    .map(ProductPriceInCheck::getFinalPrice)
                    .orElse(effectivePrice);

            // Тип ценника
            String tagType = REGULAR_TYPE.equals(effectiveType) ? "белый" :
                    effectiveType.toLowerCase().contains("акцион") ? "акционный" : "желтый";

            // Описание акции
            String promoDescription = extractPromoDescription(effectiveType);

            priceTags.add(new PriceTag(productDto.getName(), effectivePrice, tagType, regularPrice, promoDescription));

            // Передача данных по уценке (для любого типа с "уцен" в названии)
            if (effectiveType.toLowerCase().contains("уцен")) {
                LocalDate weekAgo = today.minusDays(SALES_PERIOD_DAYS);
                Pair<Integer, Integer> stock = calculateStockByLocation(allOperations, productId, locationTypeMap);
                long soldLastWeek = calculateSoldUnitsInPeriod(allOperations, productId, weekAgo, today, locationTypeMap);
                System.out.println("Передача данных по уценке в ГК: productId=" + productId + ", type=" + effectiveType +
                        ", hallStock=" + stock.getLeft() + ", warehouseStock=" + stock.getRight() +
                        ", totalStock=" + (stock.getLeft() + stock.getRight()) + ", salesLastWeek=" + soldLastWeek);
            }
        }

        coupons.addAll(calculateNearExpiryCoupons(today, allOperations));

        if (!stopList.isEmpty()) {
            System.out.println("Передача стоп-листа в ГК: " + stopList);
        }

        return new PriceOrderResponse(updatedProducts, stopList, priceTags, coupons);
    }

    // ====================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ======================

    private BigDecimal calculateChangePercent(BigDecimal newPrice, BigDecimal oldPrice) {
        if (oldPrice.compareTo(BigDecimal.ZERO) <= 0) return BigDecimal.ONE;
        return newPrice.subtract(oldPrice).abs().divide(oldPrice, 10, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    private String extractPromoDescription(String type) {
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

    private Map<Integer, BigDecimal> getCurrentMinPrices(LocalDate date) {
        Check currentCheck = checkRepository.getReferenceById(CURRENT_PRICE_CHECK_ID);
        return productPriceInCheckRepository.findByCheck(currentCheck).stream()
                .filter(it -> !it.getPriceList().getEffectiveDate().isAfter(date) &&
                        !it.getPriceList().getEndDate().isBefore(date))
                .collect(Collectors.groupingBy(it -> it.getProduct().getProductId(),
                        Collectors.mapping(ProductPriceInCheck::getFinalPrice, Collectors.minBy(Comparator.naturalOrder()))))
                .entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }

    /**
     * Генерация автоуценки. Теперь возвращает список товаров, заблокированных из-за нарушения наценки.
     * Всегда передаёт данные в ГК (даже если заблокировано).
     */
    private List<StopListItemDto> generateAutoMarkdowns(LocalDate today, List<StockOperationDto> allOperations, Map<Integer, String> locationTypeMap) {
        List<StopListItemDto> blocked = new ArrayList<>();
        List<ProductDto> productDtos = productService.getAll();
        LocalDate weekAgo = today.minusDays(SALES_PERIOD_DAYS);
        Check currentCheck = checkRepository.getReferenceById(CURRENT_PRICE_CHECK_ID);
        Discount noDiscount = discountRepository.getReferenceById(NO_DISCOUNT_ID);

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
                        .filter(it -> REGULAR_TYPE.equals(it.getPriceType()))
                        .findFirst()
                        .orElse(null);

                if (regularRecord == null) continue;

                BigDecimal inputPrice = regularRecord.getInputPrice();
                BigDecimal markdownPrice = regularRecord.getFinalPrice().multiply(AUTO_MARKDOWN_COEFFICIENT);

                // Проверка наценки (критическая, не может быть превышена ни при каких условиях)
                Pair<BigDecimal, BigDecimal> restrictions = getRestrictionsForProduct(productDto);
                BigDecimal maxMarkupPct = restrictions.getLeft();
                BigDecimal maxAllowedMarkupPrice = inputPrice.multiply(BigDecimal.ONE.add(maxMarkupPct.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)));

                boolean markupOk = markdownPrice.compareTo(maxAllowedMarkupPrice) <= 0;

                System.out.println("Передача данных по автоуценке в ГК: product=" + productId + ", hallStock=" + stock.getLeft() +
                        ", warehouseStock=" + stock.getRight() + ", totalStock=" + totalStock + ", salesLastWeek=" + soldLastWeek +
                        (markupOk ? "" : " (ЗАБЛОКИРОВАНО: превышение лимита наценки)"));

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

    private PriceList getOrCreatePriceList(String type, LocalDate effective, LocalDate end) {
        Optional<PriceList> existing = priceListRepository.findAll().stream()
                .filter(it -> it.getType().equalsIgnoreCase(type) &&
                        !it.getEffectiveDate().isAfter(effective) &&
                        !it.getEndDate().isBefore(effective))
                .findFirst();

        if (existing.isPresent()) {
            return existing.get();
        }

        PriceList newPl = new PriceList();
        newPl.setType(type);
        newPl.setEffectiveDate(effective);
        newPl.setEndDate(end == null ? effective.plusYears(10) : end);
        return priceListRepository.save(newPl);
    }

    private Pair<Integer, Integer> calculateStockByLocation(List<StockOperationDto> allOperations, int productId, Map<Integer, String> locationTypeMap) {
        int hall = 0;
        int warehouse = 0;
        for (StockOperationDto op : allOperations) {
            if (op.getProductId() == productId) {
                int amount = ADD_OPERATION_TYPE.equalsIgnoreCase(op.getOperationType()) ? op.getQuantity() : -op.getQuantity();
                String locType = locationTypeMap.getOrDefault(op.getStorageLocationId(), "");
                if ("trading_hall".equalsIgnoreCase(locType)) {
                    hall += amount;
                } else {
                    warehouse += amount;
                }
            }
        }
        return new Pair<>(Math.max(hall, 0), Math.max(warehouse, 0));
    }

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

    private Pair<BigDecimal, BigDecimal> getRestrictionsForProduct(ProductDto productDto) {
        String nameLower = productDto.getName().toLowerCase();
        return restrictedKeywordsToRestrictions.entrySet().stream()
                .filter(entry -> nameLower.contains(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(defaultRestriction);
    }

    private List<CouponDto> calculateNearExpiryCoupons(LocalDate today, List<StockOperationDto> allOperations) {
        List<CouponDto> coupons = new ArrayList<>();

        List<StockOperationDto> candidateAdds = allOperations.stream()
                .filter(it -> ADD_OPERATION_TYPE.equalsIgnoreCase(it.getOperationType()) &&
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
                        batch.expiryDate.isAfter(today) &&
                        !batch.expiryDate.isAfter(today.plusDays(NEAR_EXPIRY_DAYS))) {
                    coupons.add(new CouponDto(batch.id, batch.expiryDate, COUPON_DISCOUNT,
                            "Близкий срок годности (осталось " + batch.remaining + " шт.) — скидка " +
                                    COUPON_DISCOUNT.multiply(BigDecimal.valueOf(100)) + "%"));
                }
            }
        }

        return coupons;
    }

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