package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ShelfDto;
import ru.market.inventory_service.service.ShelfService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/shelves")
@AllArgsConstructor
public class ShelfController {

    private final ShelfService shelfService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ShelfDto>>> getAllCounterparties() {
        return shelfService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ShelfDto>> getShelfById(@PathVariable Integer id) {
        return shelfService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ShelfDto>> addShelf(@RequestBody ShelfDto shelfDto) {
        return shelfService.add(shelfDto).thenApply(
                result -> ResponseEntity.status(HttpStatus.CREATED).body(result)
        );
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ShelfDto>> updateShelfById(@PathVariable Integer id, @RequestBody ShelfDto shelfDto) {
        return shelfService.updateById(id, shelfDto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteShelfById(@PathVariable Integer id) {
        return shelfService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
