package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.InventoryDto;
import ru.market.inventory_service.service.InventoryService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/inventory")
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<InventoryDto>>> getAllCounterparties() {
        return inventoryService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<InventoryDto>> getInventoryById(@PathVariable Integer id) {
        return inventoryService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<InventoryDto>> addInventory(@RequestBody InventoryDto inventoryDto) {
        return inventoryService.add(inventoryDto).thenApply(
                result -> ResponseEntity.status(HttpStatus.CREATED).body(result)
        );
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<InventoryDto>> updateInventoryById(@PathVariable Integer id, @RequestBody InventoryDto inventoryDto) {
        return inventoryService.updateById(id, inventoryDto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteInventoryById(@PathVariable Integer id) {
        return inventoryService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
