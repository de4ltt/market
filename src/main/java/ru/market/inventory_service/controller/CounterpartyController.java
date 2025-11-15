package ru.market.inventory_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.market.inventory_service.exception.FailedToRetrieveCounterpartiesException;
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
        return counterpartyService.getAllCounterparties().handle((result, throwable) -> {
            if (throwable == null)
                throw new FailedToRetrieveCounterpartiesException();
            else return ResponseEntity.ok(result);
        });
    }
}
