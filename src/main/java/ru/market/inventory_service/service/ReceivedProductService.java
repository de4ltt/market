package ru.market.inventory_service.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.core.service.MarketInventoryCRUDService;
import ru.market.inventory_service.exception.*;
import ru.market.inventory_service.mapper.ReceivedProductMapper;
import ru.market.inventory_service.model.dto.ProductStorageDto;
import ru.market.inventory_service.model.dto.ReceivedProductDto;
import ru.market.inventory_service.model.dto.StockOperationDto;
import ru.market.inventory_service.model.entity.ReceivedProduct;
import ru.market.inventory_service.model.entity.StorageLocation;
import ru.market.inventory_service.repository.ReceivedProductRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Service
public class ReceivedProductService extends MarketInventoryCRUDService<ReceivedProduct, ReceivedProductDto> {
    private final ReceivedProductRepository receivedProductRepository;
    private final ReceivedProductMapper receivedProductMapper;
    private final StorageLocationService storageLocationService;
    private final StockOperationService stockOperationService;
    private final ShelfService shelfService; // Added
    private final ProductStorageService productStorageService; // Added
    private final ProductService productService; // Added

    @Autowired
    public ReceivedProductService(
            ReceivedProductRepository receivedProductRepository,
            ReceivedProductMapper receivedProductMapper,
            StorageLocationService storageLocationService,
            StockOperationService stockOperationService,
            ShelfService shelfService,
            ProductStorageService productStorageService,
            ProductService productService
    ) {
        super(receivedProductRepository, receivedProductMapper);
        this.receivedProductRepository = receivedProductRepository;
        this.receivedProductMapper = receivedProductMapper;
        this.storageLocationService = storageLocationService;
        this.stockOperationService = stockOperationService;
        this.shelfService = shelfService;
        this.productStorageService = productStorageService;
        this.productService = productService;
    }

    @Override
    @Transactional
    public ReceivedProductDto add(ReceivedProductDto product) {
        try {
            product.setStatus(ReceivedProductStatus.ARRIVED.getName());
            return receivedProductMapper.toDto(
                    receivedProductRepository.save(receivedProductMapper.toEntity(product))
            );
        } catch (Exception e) {
            throw new FailedToSaveEntityException(ReceivedProduct.class.getSimpleName());
        }
    }

    @Transactional(readOnly = true)
    public List<ReceivedProductDto> getArrivedProducts() {
        try {
            return receivedProductRepository.findByStatus(ReceivedProductStatus.ARRIVED.getName())
                    .stream().map(receivedProductMapper::toDto).toList();
        } catch (Exception e) {
            throw new EntitiesRetrieveException(ReceivedProduct.class.getSimpleName());
        }
    }

    @Transactional
    public void refuseProducts(List<ReceivedProductDto> products) {
        saveProductsWithStatus(products, ReceivedProductStatus.REFUSED);
    }

    @Transactional
    public void acceptProducts(Integer employeeId, List<ReceivedProductDto> products) {
        try {
            final Integer marketStorageLocationId;
            try {
                marketStorageLocationId = storageLocationService
                        .getStorageByType(StorageLocationService.StorageType.MARKET).getStorageLocationId();
            } catch (Exception e) {
                throw new EntityRetrieveException(StorageLocation.class.getSimpleName(), 0);
            }
            Function<List<ReceivedProductDto>, List<StockOperationDto>> mapStoredProducts = productList ->
                    productList.stream().map(receivedProductDto -> {
                        StockOperationDto stockOperation = new StockOperationDto();
                        stockOperation.setProductId(receivedProductDto.getReceivedProductId());
                        stockOperation.setResponsibleEmployeeId(employeeId);
                        stockOperation.setStorageLocationId(marketStorageLocationId);
                        stockOperation.setShelfId(null);
                        stockOperation.setQuantity(receivedProductDto.getQuantity());
                        stockOperation.setOperationType(StockOperationService.StockOperationType.ADD.getType());
                        stockOperation.setExpiryDate(receivedProductDto.getExpirationDate());
                        stockOperation.setReason("Accepted from receipt");
                        stockOperation.setTimestamp(LocalDate.now());
                        return stockOperation;
                    }).toList();
            stockOperationService.addAll(mapStoredProducts.apply(products));
            saveProductsWithStatus(products, ReceivedProductStatus.ACCEPTED);
// Auto-assign to shelves based on storage requirement
            List<Integer> productIds = products.stream()
                    .map(ReceivedProductDto::getReceivedProductId)
                    .toList();
            autoAssignToShelves(productIds);
        } catch (Exception e) {
            throw new FailedToSaveEntityException(ReceivedProduct.class.getSimpleName());
        }
    }

    private void autoAssignToShelves(List<Integer> productIds) {
        var refrigeratedIds = productIds.stream()
                .filter(pid -> "refrigerated".equals(productService.getById(pid).getStorageRequirement()))
                .toList();
        var regularIds = productIds.stream()
                .filter(pid -> "regular".equals(productService.getById(pid).getStorageRequirement()))
                .toList();
        if (!refrigeratedIds.isEmpty()) {
            assignToRefrigerator(refrigeratedIds);
        }
        if (!regularIds.isEmpty()) {
            assignToRegularShelf(regularIds);
        }
    }

    private void assignToRefrigerator(List<Integer> productIds) {
        var refrigeratorShelves = shelfService.getAll().stream()
                .filter(s -> "refrigerator".equals(s.getType())).toList();
        if (refrigeratorShelves.isEmpty()) {
            throw new EntityNotFoundException("Shelf", 0);
        }
        var shelfId = refrigeratorShelves.get(0).getShelfId();
        productIds.forEach(pid -> {
            ProductStorageDto ps = new ProductStorageDto();
            ps.setShelfId(shelfId);
            ps.setProductId(pid);
            productStorageService.add(ps);
        });
    }

    private void assignToRegularShelf(List<Integer> productIds) {
        var regularShelves = shelfService.getAll().stream()
                .filter(s -> "regular".equals(s.getType())).toList();
        if (regularShelves.isEmpty()) {
            throw new EntityNotFoundException("Shelf", 0);
        }
        var shelfId = regularShelves.get(0).getShelfId();
        productIds.forEach(pid -> {
            ProductStorageDto ps = new ProductStorageDto();
            ps.setShelfId(shelfId);
            ps.setProductId(pid);
            productStorageService.add(ps);
        });
    }

    @Transactional
    private void saveProductsWithStatus(List<ReceivedProductDto> products, ReceivedProductStatus status) throws FailedToSaveEntitiesException {
        try {
            receivedProductRepository.saveAll(
                    products.stream()
                            .map(receivedProductMapper::toEntity)
                            .peek(product -> product.setStatus(status.getName()))
                            .toList()
            );
        } catch (Exception e) {
            throw new FailedToSaveEntitiesException(ReceivedProduct.class.getSimpleName());
        }
    }

    @Getter
    private enum ReceivedProductStatus {
        ARRIVED("arrived"),
        ACCEPTED("accepted"),
        REFUSED("refused");
        private final String name;

        ReceivedProductStatus(String name) {
            this.name = name;
        }
    }
}