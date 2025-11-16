package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.inventory_service.model.dto.StorageLocationDto;
import ru.market.inventory_service.model.entity.StorageLocation;

@Mapper(componentModel = "spring")
public interface StorageLocationMapper extends EntityMapper<StorageLocation, StorageLocationDto> {
    StorageLocationDto toDto(StorageLocation entity);
    StorageLocation toEntity(StorageLocationDto dto);
}
