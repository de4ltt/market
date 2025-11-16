package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.SupplyContractDto;
import ru.market.inventory_service.service.SupplyContractService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/supply-contracts")
@AllArgsConstructor
public class SupplyContractController {

    private final SupplyContractService supplyContractService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<SupplyContractDto>>> getAllCounterparties() {
        return supplyContractService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<SupplyContractDto>> getSupplyContractById(@PathVariable Integer id) {
        return supplyContractService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<SupplyContractDto>> addSupplyContract(@RequestBody SupplyContractDto supplyContractDto) {
        return supplyContractService.add(supplyContractDto).thenApply(
                result -> ResponseEntity.status(HttpStatus.CREATED).body(result)
        );
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<SupplyContractDto>> updateSupplyContractById(@PathVariable Integer id, @RequestBody SupplyContractDto supplyContractDto) {
        return supplyContractService.updateById(id, supplyContractDto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteSupplyContractById(@PathVariable Integer id) {
        return supplyContractService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
