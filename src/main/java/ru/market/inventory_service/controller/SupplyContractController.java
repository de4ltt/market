package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.SupplyContractDto;
import ru.market.inventory_service.service.SupplyContractService;

import java.util.List;

@RestController
@RequestMapping("/supply-contracts")
@AllArgsConstructor
public class SupplyContractController {

    private final SupplyContractService supplyContractService;

    @GetMapping
    public ResponseEntity<List<SupplyContractDto>> getAllCounterparties() {
        return ResponseEntity.ok(supplyContractService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyContractDto> getSupplyContractById(@PathVariable Integer id) {
        return ResponseEntity.ok(supplyContractService.getById(id));
    }

    @PostMapping
    public ResponseEntity<SupplyContractDto> addSupplyContract(@RequestBody SupplyContractDto supplyContractDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplyContractService.add(supplyContractDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyContractDto> updateSupplyContractById(@PathVariable Integer id, @RequestBody SupplyContractDto supplyContractDto) {
        return ResponseEntity.ok(supplyContractService.updateById(id, supplyContractDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplyContractById(@PathVariable Integer id) {
        supplyContractService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
