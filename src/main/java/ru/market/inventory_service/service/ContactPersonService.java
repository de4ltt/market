package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.MarketInventoryCRUDService;
import ru.market.inventory_service.mapper.ContactPersonMapper;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.model.entity.ContactPerson;
import ru.market.inventory_service.repository.ContactPersonRepository;

@Service
public class ContactPersonService extends MarketInventoryCRUDService<ContactPerson, ContactPersonDto> {
    @Autowired
    public ContactPersonService(ContactPersonRepository contactPersonRepository, ContactPersonMapper contactPersonMapper) {
        super(contactPersonRepository, contactPersonMapper);
    }
}
