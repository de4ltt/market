package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.market.inventory_service.model.dto.CounterpartyDto;
import ru.market.inventory_service.model.entity.Counterparty;

@Mapper(componentModel = "spring")
public interface CounterpartyMapper {

    CounterpartyMapper INSTANCE = Mappers.getMapper(CounterpartyMapper.class);

    CounterpartyDto toDto(Counterparty entity);

    Counterparty toEntity(CounterpartyDto dto);
}
