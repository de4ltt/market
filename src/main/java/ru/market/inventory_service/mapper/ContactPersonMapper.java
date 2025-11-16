package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.model.entity.ContactPerson;
import ru.market.inventory_service.repository.CounterpartyRepository;

@Mapper(componentModel = "spring", uses = {CounterpartyRepository.class})
public interface ContactPersonMapper extends EntityMapper<ContactPerson, ContactPersonDto> {

    @Mapping(target = "counterpartyId", source = "counterparty.counterpartyId")
    ContactPersonDto toDto(ContactPerson entity);

    @Mapping(target = "counterparty", source = "counterpartyId", qualifiedByName = "counterpartyById")
    ContactPerson toEntity(ContactPersonDto dto);
}
