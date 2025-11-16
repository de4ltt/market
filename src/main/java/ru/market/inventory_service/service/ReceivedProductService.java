package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.InventoryCRUDService;
import ru.market.inventory_service.mapper.ReceivedProductMapper;
import ru.market.inventory_service.model.dto.ReceivedProductDto;
import ru.market.inventory_service.model.entity.ReceivedProduct;
import ru.market.inventory_service.repository.ReceivedProductRepository;

@Service
public class ReceivedProductService extends InventoryCRUDService<ReceivedProduct, ReceivedProductDto> {
    @Autowired
    public ReceivedProductService(ReceivedProductRepository receivedProductRepository, ReceivedProductMapper receivedProductMapper) {
        super(receivedProductRepository, receivedProductMapper);
    }
}
