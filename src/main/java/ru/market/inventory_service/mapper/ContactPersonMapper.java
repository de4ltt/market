package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.market.inventory_service.model.dto.ContactPersonDto;
import ru.market.inventory_service.model.entity.ContactPerson;

@Mapper(componentModel = "spring")
public interface ContactPersonMapper {

    ContactPersonMapper INSTANCE = Mappers.getMapper(ContactPersonMapper.class);

    ContactPersonDto toDto(ContactPerson entity);

    ContactPerson toEntity(ContactPersonDto dto);
}
