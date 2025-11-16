package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.inventory_service.model.dto.SupplyContractDto;
import ru.market.inventory_service.model.entity.SupplyContract;
import ru.market.inventory_service.repository.CounterpartyRepository;
import ru.market.inventory_service.repository.StorageLocationRepository;

@Mapper(componentModel = "spring",
        uses = {CounterpartyRepository.class, StorageLocationRepository.class})
public interface SupplyContractMapper extends EntityMapper<SupplyContract, SupplyContractDto> {

    @Mapping(target = "contractorId",      source = "contractor.counterpartyId")
    @Mapping(target = "storageLocationId", source = "storageLocation.storageLocationId")
    SupplyContractDto toDto(SupplyContract entity);

    @Mapping(target = "contractor",       source = "contractorId",      qualifiedByName = "counterpartyById")
    @Mapping(target = "storageLocation", source = "storageLocationId", qualifiedByName = "storageLocationById")
    SupplyContract toEntity(SupplyContractDto dto);
}