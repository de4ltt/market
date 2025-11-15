package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.inventory_service.model.dto.InventoryDto;
import ru.market.inventory_service.model.entity.Inventory;
import ru.market.inventory_service.repository.ProductRepository;

@Mapper(componentModel = "spring",
        uses = {EmployeeRepository.class, ProductRepository.class})
public interface InventoryMapper {

    @Mapping(target = "employeeId", source = "employee.employeeId")
    @Mapping(target = "productId",   source = "product.productId")
    InventoryDto toDto(Inventory entity);

    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "employeeById")
    @Mapping(target = "product",   source = "productId",   qualifiedByName = "productById")
    Inventory toEntity(InventoryDto dto);
}