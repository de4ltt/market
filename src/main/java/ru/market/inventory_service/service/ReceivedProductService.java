package ru.market.inventory_service.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.core.service.MarketInventoryCRUDService;
import ru.market.inventory_service.exception.EntitiesRetrieveException;
import ru.market.inventory_service.exception.EntityRetrieveException;
import ru.market.inventory_service.exception.FailedToSaveEntitiesException;
import ru.market.inventory_service.exception.FailedToSaveEntityException;
import ru.market.inventory_service.mapper.ReceivedProductMapper;
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

    @Autowired
    public ReceivedProductService(
            ReceivedProductRepository receivedProductRepository,
            ReceivedProductMapper receivedProductMapper,
            StorageLocationService storageLocationService,
            StockOperationService stockOperationService
    ) {
        super(receivedProductRepository, receivedProductMapper);
        this.receivedProductRepository = receivedProductRepository;
        this.receivedProductMapper = receivedProductMapper;
        this.storageLocationService = storageLocationService;
        this.stockOperationService = stockOperationService;
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
                    productList.parallelStream().map(receivedProductDto -> {
                        StockOperationDto stockOperation = new StockOperationDto();
                        stockOperation.setProductId(receivedProductDto.getReceivedProductId());
                        stockOperation.setResponsibleEmployeeId(employeeId);
                        stockOperation.setStorageLocationId(marketStorageLocationId);
                        stockOperation.setShelfId(null);
                        stockOperation.setQuantity(receivedProductDto.getQuantity());
                        stockOperation.setOperationType(StockOperationService.StockOperationType.ADD.getType());
                        stockOperation.setExpiryDate(receivedProductDto.getExpirationDate());
                        stockOperation.setTimestamp(LocalDate.now());
                        return stockOperation;
                    }).toList();

            stockOperationService.addAll(mapStoredProducts.apply(products));
            saveProductsWithStatus(products, ReceivedProductStatus.ACCEPTED);
        } catch (Exception e) {
            throw new FailedToSaveEntityException(ReceivedProduct.class.getSimpleName());
        }
    }

    @Transactional
    private void saveProductsWithStatus(List<ReceivedProductDto> products, ReceivedProductStatus status) throws FailedToSaveEntitiesException {
        try {
            receivedProductRepository.saveAll(
                    products.parallelStream()
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
