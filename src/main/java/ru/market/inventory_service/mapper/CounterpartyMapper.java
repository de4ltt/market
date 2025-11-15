package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.inventory_service.model.dto.CounterpartyDto;
import ru.market.inventory_service.model.entity.Counterparty;

@Mapper(componentModel = "spring")
public interface CounterpartyMapper extends EntityMapper<Counterparty, CounterpartyDto> {
    CounterpartyDto toDto(Counterparty entity);
    Counterparty toEntity(CounterpartyDto dto);
}
