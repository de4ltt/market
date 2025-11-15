package ru.market.inventory_service.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.exception.CounterpartyNotFoundException;
import ru.market.inventory_service.exception.RetrieveCounterpartiesException;
import ru.market.inventory_service.mapper.ContactPersonMapper;
import ru.market.inventory_service.mapper.CounterpartyMapper;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.model.dto.CounterpartyDto;
import ru.market.inventory_service.model.entity.Counterparty;
import ru.market.inventory_service.repository.CounterpartyRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class CounterpartyService {

    private final CounterpartyRepository counterpartyRepository;

    private final CounterpartyMapper counterpartyMapper;
    private final ContactPersonMapper contactPersonMapper;

    @Async
    @Transactional
    public CompletableFuture<List<CounterpartyDto>> getAllCounterparties() {
        return CompletableFuture.completedFuture(
                counterpartyRepository.findAll().parallelStream().map(counterpartyMapper::toDto).toList()
        ).handle((result, throwable) -> {
            if (throwable != null)
                throw new RetrieveCounterpartiesException();
            return result;
        });
    }

    @Async
    @Transactional
    public CompletableFuture<List<ContactPersonDto>> getCounterpartyContactsById(Integer id) {
        Optional<Counterparty> counterparty = counterpartyRepository.findById(id);
        if (counterparty.isPresent())
            return CompletableFuture.completedFuture(
                    counterparty.get().getContactPersonList().parallelStream().map(contactPersonMapper::toDto).toList()
            );
        else
            throw new CounterpartyNotFoundException(id);
    }
}
