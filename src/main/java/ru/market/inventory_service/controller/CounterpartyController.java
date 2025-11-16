package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.model.dto.CounterpartyDto;
import ru.market.inventory_service.service.CounterpartyService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/counterparties")
@AllArgsConstructor
public class CounterpartyController {

    private final CounterpartyService counterpartyService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<CounterpartyDto>>> getAllCounterparties() {
        return counterpartyService.getAll().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<CounterpartyDto>> getCounterpartyById(@PathVariable Integer id) {
        return counterpartyService.getById(id).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<CounterpartyDto>> addCounterparty(@RequestBody CounterpartyDto counterpartyDto) {
        return counterpartyService.add(counterpartyDto).thenApply(
                result -> ResponseEntity.status(HttpStatus.CREATED).body(result)
        );
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<CounterpartyDto>> updateCounterpartyById(@PathVariable Integer id, @RequestBody CounterpartyDto counterpartyDto) {
        return counterpartyService.updateById(id, counterpartyDto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteCounterpartyById(@PathVariable Integer id) {
        return counterpartyService.deleteById(id).thenApply((ignored) -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping("/{id}/contacts")
    public CompletableFuture<ResponseEntity<List<ContactPersonDto>>> getCounterpartyContactsById(@PathVariable Integer id) {
        return counterpartyService.getCounterpartyContactsById(id).thenApply(ResponseEntity::ok);
    }
}
