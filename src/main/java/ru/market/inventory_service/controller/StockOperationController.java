package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.StockOperationDto;
import ru.market.inventory_service.service.StockOperationService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/stock-operations")
@AllArgsConstructor
public class StockOperationController {

    private final StockOperationService stockOperationService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<StockOperationDto>>> getAllCounterparties() {
        return stockOperationService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<StockOperationDto>> getStockOperationById(@PathVariable Integer id) {
        return stockOperationService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<StockOperationDto>> addStockOperation(@RequestBody StockOperationDto stockOperationDto) {
        return stockOperationService.add(stockOperationDto).thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<StockOperationDto>> updateStockOperationById(@PathVariable Integer id, @RequestBody StockOperationDto stockOperationDto) {
        return stockOperationService.updateById(id, stockOperationDto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteStockOperationById(@PathVariable Integer id) {
        return stockOperationService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
