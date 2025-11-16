package ru.market.inventory_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.InventoryCRUDService;
import ru.market.inventory_service.mapper.InventoryMapper;
import ru.market.inventory_service.model.dto.InventoryDto;
import ru.market.inventory_service.model.entity.Inventory;
import ru.market.inventory_service.repository.InventoryRepository;

@Service
public class InventoryService extends InventoryCRUDService<Inventory, InventoryDto> {
    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        super(inventoryRepository, inventoryMapper);
    }
}