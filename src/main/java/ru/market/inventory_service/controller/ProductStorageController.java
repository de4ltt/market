package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ProductStorageDto;
import ru.market.inventory_service.service.ProductStorageService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/product-storage")
@AllArgsConstructor
public class ProductStorageController {

    private final ProductStorageService productStorageService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ProductStorageDto>>> getAllCounterparties() {
        return productStorageService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{if}")
    public CompletableFuture<ResponseEntity<ProductStorageDto>> getProductStorageById(@PathVariable Integer id) {
        return productStorageService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ProductStorageDto>> addProductStorage(@RequestBody ProductStorageDto productStorageDto) {
        return productStorageService.add(productStorageDto).thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductStorageDto>> updateProductStorageById(@PathVariable Integer id, @RequestBody ProductStorageDto productStorageDto) {
        return productStorageService.updateById(id, productStorageDto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteProductStorageById(@PathVariable Integer id) {
        return productStorageService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
