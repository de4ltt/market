package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.market.inventory_service.model.dto.StorageLocationDto;
import ru.market.inventory_service.service.StorageLocationService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/storage-locations")
@AllArgsConstructor
public class StorageLocationController {

    private final StorageLocationService storageLocationService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<StorageLocationDto>>> getAllStorageLocations() {
        return storageLocationService.getAllStorageLocations().thenApply(ResponseEntity::ok);
    }
}
