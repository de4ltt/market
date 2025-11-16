package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ReceivedProductDto;
import ru.market.inventory_service.service.ReceivedProductService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/received-product")
@AllArgsConstructor
public class ReceivedProductController {

    private final ReceivedProductService receivedProductService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ReceivedProductDto>>> getAllCounterparties() {
        return receivedProductService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{if}")
    public CompletableFuture<ResponseEntity<ReceivedProductDto>> getReceivedProductById(@PathVariable Integer id) {
        return receivedProductService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<ReceivedProductDto>> addReceivedProduct(@RequestBody ReceivedProductDto receivedProductDto) {
        return receivedProductService.add(receivedProductDto).thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ReceivedProductDto>> updateReceivedProductById(@PathVariable Integer id, @RequestBody ReceivedProductDto receivedProductDto) {
        return receivedProductService.updateById(id, receivedProductDto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteReceivedProductById(@PathVariable Integer id) {
        return receivedProductService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
