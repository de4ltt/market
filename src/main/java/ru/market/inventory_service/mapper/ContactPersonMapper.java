package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.model.entity.ContactPerson;

@Mapper(componentModel = "spring")
public interface ContactPersonMapper {

    ContactPersonDto toDto(ContactPerson entity);

    ContactPerson toEntity(ContactPersonDto dto);
}
