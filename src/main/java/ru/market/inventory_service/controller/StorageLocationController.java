package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.StorageLocationDto;
import ru.market.inventory_service.service.StorageLocationService;

import java.util.List;

@RestController
@RequestMapping("/storage-locations")
@AllArgsConstructor
public class StorageLocationController {

    private final StorageLocationService storageLocationService;

    @GetMapping
    public ResponseEntity<List<StorageLocationDto>> getAllStorageLocations() {
        return ResponseEntity.ok(storageLocationService.getAll());
    }

    @GetMapping("/market")
    public ResponseEntity<StorageLocationDto> getMarketStorage() {
        return ResponseEntity.ok(storageLocationService.getStorageByType(StorageLocationService.StorageType.MARKET));
    }

    @GetMapping("/hub")
    public ResponseEntity<StorageLocationDto> getHubStorage() {
        return ResponseEntity.ok(storageLocationService.getStorageByType(StorageLocationService.StorageType.HUB));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StorageLocationDto> getStorageLocationById(@PathVariable Integer id) {
        return ResponseEntity.ok(storageLocationService.getById(id));
    }

    @PostMapping
    public ResponseEntity<StorageLocationDto> addStorageLocation(@RequestBody StorageLocationDto storageLocation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storageLocationService.add(storageLocation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StorageLocationDto> updateStorageLocation(
            @PathVariable Integer id,
            @RequestBody StorageLocationDto storageLocation
    ) { return ResponseEntity.ok(storageLocationService.updateById(id, storageLocation)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorageLocationById(@PathVariable Integer id) {
        storageLocationService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
