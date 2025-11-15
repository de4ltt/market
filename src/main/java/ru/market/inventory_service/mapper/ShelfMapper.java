package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.inventory_service.model.dto.ShelfDto;
import ru.market.inventory_service.model.entity.Shelf;
import ru.market.inventory_service.repository.StorageLocationRepository;

@Mapper(componentModel = "spring", uses = {StorageLocationRepository.class})
public interface ShelfMapper {

    @Mapping(target = "storageLocationId", source = "storageLocation.storageLocationId")
    ShelfDto toDto(Shelf entity);

    @Mapping(target = "storageLocation", source = "storageLocationId", qualifiedByName = "storageLocationById")
    Shelf toEntity(ShelfDto dto);
}