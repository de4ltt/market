package ru.market.inventory_service.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.exception.RetrieveStorageLocationsException;
import ru.market.inventory_service.mapper.StorageLocationMapper;
import ru.market.inventory_service.model.dto.StorageLocationDto;
import ru.market.inventory_service.repository.StorageLocationRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class StorageLocationService {

    private final StorageLocationRepository storageLocationRepository;

    private final StorageLocationMapper storageLocationMapper;

    public CompletableFuture<List<StorageLocationDto>> getAllStorageLocations() {
        return CompletableFuture.completedFuture(
                storageLocationRepository.findAll().parallelStream().map(storageLocationMapper::toDto).toList()
        ).handle((result, throwable) -> {
            if (throwable != null)
                throw new RetrieveStorageLocationsException();
            return result;
        });
    }
}
