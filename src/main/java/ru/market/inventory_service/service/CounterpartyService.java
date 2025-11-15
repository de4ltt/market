package ru.market.inventory_service.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.mapper.CounterpartyMapper;
import ru.market.inventory_service.model.dto.CounterpartyDto;
import ru.market.inventory_service.repository.CounterpartyRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class CounterpartyService {

    private final CounterpartyRepository counterpartyRepository;
    private final CounterpartyMapper counterpartyMapper;

    @Async
    @Transactional
    public CompletableFuture<List<CounterpartyDto>> getAllCounterparties() {
        return CompletableFuture.supplyAsync(() ->
                counterpartyRepository.findAll().parallelStream().map(counterpartyMapper::toDto).toList()
        );
    }


}
