package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.StorageLocationDto;
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
        return storageLocationService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<StorageLocationDto>> getStorageLocationById(@PathVariable Integer id) {
        return storageLocationService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<StorageLocationDto>> addStorageLocation(@RequestBody StorageLocationDto storageLocation) {
        return storageLocationService.add(storageLocation).thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<StorageLocationDto>> updateStorageLocation(
            @PathVariable Integer id,
            @RequestBody StorageLocationDto storageLocation
    ) { return storageLocationService.updateById(id, storageLocation).thenApply(ResponseEntity::ok); }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteStorageLocationById(@PathVariable Integer id) {
        return storageLocationService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
