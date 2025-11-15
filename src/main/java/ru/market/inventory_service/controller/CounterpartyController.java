package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.market.inventory_service.exception.FailedToRetrieveCounterpartiesException;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.model.dto.CounterpartyDto;
import ru.market.inventory_service.service.CounterpartyService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/counteragents")
@AllArgsConstructor
public class CounterpartyController {

    private final CounterpartyService counterpartyService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<CounterpartyDto>>> getAllCounterparties() {
        return counterpartyService.getAllCounterparties().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}/contacts")
    public CompletableFuture<ResponseEntity<List<ContactPersonDto>>> getCounterpartyContactsById(@PathVariable Integer id) {
        return counterpartyService.getCounterpartyContactsById(id).thenApply(ResponseEntity::ok);
    }
}
