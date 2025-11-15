package ru.market.hr_service.mapper;

import org.mapstruct.Mapper;
import ru.market.hr_service.model.dto.PositionDto;
import ru.market.hr_service.model.entity.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {
    PositionDto toDto(Position entity);
    Position toEntity(PositionDto dto);
}
