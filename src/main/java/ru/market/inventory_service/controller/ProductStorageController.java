package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ProductStorageDto;
import ru.market.inventory_service.service.ProductStorageService;

import java.util.List;

@RestController
@RequestMapping("/product-storage")
@AllArgsConstructor
public class ProductStorageController {

    private final ProductStorageService productStorageService;

    @GetMapping
    public ResponseEntity<List<ProductStorageDto>> getAllCounterparties() {
        return ResponseEntity.ok(productStorageService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductStorageDto> getProductStorageById(@PathVariable Integer id) {
        return ResponseEntity.ok(productStorageService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ProductStorageDto> addProductStorage(@RequestBody ProductStorageDto productStorageDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productStorageService.add(productStorageDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductStorageDto> updateProductStorageById(@PathVariable Integer id, @RequestBody ProductStorageDto productStorageDto) {
        return ResponseEntity.ok(productStorageService.updateById(id, productStorageDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductStorageById(@PathVariable Integer id) {
        productStorageService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
