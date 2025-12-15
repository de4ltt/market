package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.StockOperationDto;
import ru.market.inventory_service.service.StockOperationService;

import java.util.List;

@RestController
@RequestMapping("/stock-operations")
@AllArgsConstructor
public class StockOperationController {

    private final StockOperationService stockOperationService;

    @GetMapping
    public ResponseEntity<List<StockOperationDto>> getAllCounterparties() {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockOperationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockOperationDto> getStockOperationById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockOperationService.getById(id));
    }

    @PostMapping
    public ResponseEntity<StockOperationDto> addStockOperation(@RequestBody StockOperationDto stockOperationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockOperationService.add(stockOperationDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockOperationDto> updateStockOperationById(@PathVariable Integer id, @RequestBody StockOperationDto stockOperationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockOperationService.updateById(id, stockOperationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockOperationById(@PathVariable Integer id) {
        stockOperationService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

//TODO