package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ProductStorageDto;
import ru.market.inventory_service.model.dto.ShelfDto;
import ru.market.inventory_service.service.ProductService;
import ru.market.inventory_service.service.ProductStorageService;
import ru.market.inventory_service.service.ShelfService;

import java.util.List;

@RestController
@RequestMapping("/shelves")
@AllArgsConstructor
public class ShelfController {

    private final ShelfService shelfService;
    private final ProductStorageService productStorageService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ShelfDto>> getAll() {
        return ResponseEntity.ok(shelfService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelfDto> getShelfById(@PathVariable Integer id) {
        return ResponseEntity.ok(shelfService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ShelfDto> addShelf(@RequestBody ShelfDto shelfDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shelfService.add(shelfDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelfDto> updateShelfById(@PathVariable Integer id, @RequestBody ShelfDto shelfDto) {
        return ResponseEntity.ok(shelfService.updateById(id, shelfDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelfById(@PathVariable Integer id) {
        shelfService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/assign-refrigerated")
    public void assignToRefrigerator(@RequestBody List<Integer> productIds) {
        var refrigeratorShelves = shelfService.getAll().stream()
                .filter(s -> "refrigerator".equals(s.getType())).toList();
        if (refrigeratorShelves.isEmpty()) {
            ResponseEntity.badRequest().build();
            return;
        }
        var shelfId = refrigeratorShelves.get(0).getShelfId();
        productIds.forEach(pid -> {
            var product = productService.getById(pid);
            if ("refrigerated".equals(product.getStorageRequirement())) {
                ProductStorageDto ps = new ProductStorageDto();
                ps.setShelfId(shelfId);
                ps.setProductId(pid);
                productStorageService.add(ps);
            }
        });
        ResponseEntity.ok().build();
    }

    @PostMapping("/assign-regular")
    public void assignToRegularShelf(@RequestBody List<Integer> productIds) {
        var regularShelves = shelfService.getAll().stream()
                .filter(s -> "regular".equals(s.getType())).toList();
        if (regularShelves.isEmpty()) {
            ResponseEntity.badRequest().build();
            return;
        }
        var shelfId = regularShelves.get(0).getShelfId();
        productIds.forEach(pid -> {
            var product = productService.getById(pid);
            if ("regular".equals(product.getStorageRequirement())) {
                ProductStorageDto ps = new ProductStorageDto();
                ps.setShelfId(shelfId);
                ps.setProductId(pid);
                productStorageService.add(ps);
            }
        });
        ResponseEntity.ok().build();
    }

    @PostMapping("/auto-assign")
    public ResponseEntity<Void> autoAssignProducts(@RequestBody List<Integer> productIds) {
        var refrigeratedIds = productIds.stream()
                .filter(pid -> "refrigerated".equals(productService.getById(pid).getStorageRequirement()))
                .toList();
        var regularIds = productIds.stream()
                .filter(pid -> "regular".equals(productService.getById(pid).getStorageRequirement()))
                .toList();
        if (!refrigeratedIds.isEmpty()) {
            assignToRefrigerator(refrigeratedIds);
        }
        if (!regularIds.isEmpty()) {
            assignToRegularShelf(regularIds);
        }
        return ResponseEntity.ok().build();
    }
}