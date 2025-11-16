package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.core.service.InventoryCRUDService;
import ru.market.inventory_service.exception.EntityNotFoundException;
import ru.market.inventory_service.mapper.ContactPersonMapper;
import ru.market.inventory_service.mapper.CounterpartyMapper;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.model.dto.CounterpartyDto;
import ru.market.inventory_service.model.entity.ContactPerson;
import ru.market.inventory_service.model.entity.Counterparty;
import ru.market.inventory_service.repository.CounterpartyRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CounterpartyService extends InventoryCRUDService<Counterparty, CounterpartyDto> {

    private final CounterpartyRepository counterpartyRepository;
    private final ContactPersonMapper contactPersonMapper;

    @Autowired
    public CounterpartyService(CounterpartyRepository counterpartyRepository, CounterpartyMapper counterpartyMapper, ContactPersonMapper contactPersonMapper) {
        super(counterpartyRepository, counterpartyMapper);
        this.counterpartyRepository = counterpartyRepository;
        this.contactPersonMapper = contactPersonMapper;
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<ContactPersonDto>> getCounterpartyContactsById(Integer id) {
        Optional<Counterparty> counterparty = counterpartyRepository.findById(id);
        if (counterparty.isPresent())
            return CompletableFuture.completedFuture(
                    counterparty.get().getContactPersonList().parallelStream().map(contactPersonMapper::toDto).toList()
            );
        else throw new EntityNotFoundException(Counterparty.class.getSimpleName(), id);
    }
}
