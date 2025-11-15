package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.model.entity.ContactPerson;

@Mapper(componentModel = "spring")
public interface ContactPersonMapper {

    @Mapping(target = "counteragentId", source = "counteragent.counteragentId")
    ContactPersonDto toDto(ContactPerson entity);

    @Mapping(target = "counteragent", source = "counteragentId", qualifiedByName = "counteragentById")
    ContactPerson toEntity(ContactPersonDto dto);
}
