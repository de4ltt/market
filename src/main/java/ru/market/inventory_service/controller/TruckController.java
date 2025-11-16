package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.TruckDto;
import ru.market.inventory_service.service.TruckService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/trucks")
@AllArgsConstructor
public class TruckController {

    private final TruckService truckService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<TruckDto>>> getAllCounterparties() {
        return truckService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<TruckDto>> getTruckById(@PathVariable Integer id) {
        return truckService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<TruckDto>> addTruck(@RequestBody TruckDto truckDto) {
        return truckService.add(truckDto).thenApply(
                result -> ResponseEntity.status(HttpStatus.CREATED).body(result)
        );
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<TruckDto>> updateTruckById(@PathVariable Integer id, @RequestBody TruckDto truckDto) {
        return truckService.updateById(id, truckDto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteTruckById(@PathVariable Integer id) {
        return truckService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
