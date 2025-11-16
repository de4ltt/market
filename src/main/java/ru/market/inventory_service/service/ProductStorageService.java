package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.CRUDService;
import ru.market.inventory_service.mapper.ProductStorageMapper;
import ru.market.inventory_service.model.dto.ProductStorageDto;
import ru.market.inventory_service.model.entity.ProductStorage;
import ru.market.inventory_service.repository.ProductStorageRepository;

@Service
public class ProductStorageService extends CRUDService<ProductStorage, ProductStorageDto> {
    @Autowired
    public ProductStorageService(ProductStorageRepository productStorageRepository, ProductStorageMapper productStorageMapper) {
        super(productStorageRepository, productStorageMapper);
    }
}
