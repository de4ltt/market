package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.TruckDto;
import ru.market.inventory_service.service.TruckService;

import java.util.List;

@RestController
@RequestMapping("/trucks")
@AllArgsConstructor
public class TruckController {

    private final TruckService truckService;

    @GetMapping
    public ResponseEntity<List<TruckDto>> getAllCounterparties() {
        return ResponseEntity.ok(truckService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TruckDto> getTruckById(@PathVariable Integer id) {
        return ResponseEntity.ok(truckService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TruckDto> addTruck(@RequestBody TruckDto truckDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(truckService.add(truckDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TruckDto> updateTruckById(@PathVariable Integer id, @RequestBody TruckDto truckDto) {
        return ResponseEntity.ok(truckService.updateById(id, truckDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTruckById(@PathVariable Integer id) {
        truckService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
