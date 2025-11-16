package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.InventoryCRUDService;
import ru.market.inventory_service.mapper.StorageLocationMapper;
import ru.market.inventory_service.model.dto.StorageLocationDto;
import ru.market.inventory_service.model.entity.StorageLocation;
import ru.market.inventory_service.repository.StorageLocationRepository;

@Service
public class StorageLocationService extends InventoryCRUDService<StorageLocation, StorageLocationDto> {
    @Autowired
    public StorageLocationService(StorageLocationRepository storageLocationRepository, StorageLocationMapper storageLocationMapper) {
        super(storageLocationRepository, storageLocationMapper);
    }
}
