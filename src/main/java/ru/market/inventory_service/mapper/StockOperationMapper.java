package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.inventory_service.model.dto.StockOperationDto;
import ru.market.inventory_service.model.entity.StockOperation;
import ru.market.inventory_service.repository.ProductRepository;
import ru.market.inventory_service.repository.ShelfRepository;
import ru.market.inventory_service.repository.StorageLocationRepository;

@Mapper(componentModel = "spring",
        uses = {ProductRepository.class, EmployeeRepository.class,
                StorageLocationRepository.class, ShelfRepository.class})
public interface StockOperationMapper extends EntityMapper<StockOperation, StockOperationDto> {

    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "responsibleEmployeeId", source = "responsibleEmployee.employeeId")
    @Mapping(target = "storageLocationId", source = "storageLocation.storageLocationId")
    @Mapping(target = "shelfId", source = "shelf.shelfId")
    StockOperationDto toDto(StockOperation entity);

    @Mapping(target = "product", source = "productId", qualifiedByName = "productById")
    @Mapping(target = "responsibleEmployee",source = "responsibleEmployeeId",qualifiedByName = "employeeById")
    @Mapping(target = "storageLocation", source = "storageLocationId",   qualifiedByName = "storageLocationById")
    @Mapping(target = "shelf", source = "shelfId", qualifiedByName = "shelfById")
    StockOperation toEntity(StockOperationDto dto);
}
