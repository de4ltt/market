package ru.market.inventory_service.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.core.service.MarketInventoryCRUDService;
import ru.market.inventory_service.exception.EntityRetrieveException;
import ru.market.inventory_service.mapper.StorageLocationMapper;
import ru.market.inventory_service.model.dto.StorageLocationDto;
import ru.market.inventory_service.model.entity.StorageLocation;
import ru.market.inventory_service.repository.StorageLocationRepository;

@Service
public class StorageLocationService extends MarketInventoryCRUDService<StorageLocation, StorageLocationDto> {

    final StorageLocationRepository storageLocationRepository;
    final StorageLocationMapper storageLocationMapper;

    @Autowired
    public StorageLocationService(StorageLocationRepository storageLocationRepository, StorageLocationMapper storageLocationMapper) {
        super(storageLocationRepository, storageLocationMapper);
        this.storageLocationRepository = storageLocationRepository;
        this.storageLocationMapper = storageLocationMapper;
    }

    @Transactional(readOnly = true)
    public StorageLocationDto getStorageByType(StorageType storageType) {
        try {
            return storageLocationMapper.toDto(storageLocationRepository.findFirstByType(storageType.getTypeName()).orElseThrow());
        } catch (Exception e) {
            throw new EntityRetrieveException(StorageLocation.class.getSimpleName(), 0);
        }
    }

    @Getter
    public enum StorageType {
        HUB("hub"),
        MARKET("market");

        private final String typeName;

        StorageType(String typeName) {
            this.typeName = typeName;
        }
    }
}
