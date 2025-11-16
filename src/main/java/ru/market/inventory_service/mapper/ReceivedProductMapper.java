package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.inventory_service.model.dto.ReceivedProductDto;
import ru.market.inventory_service.model.entity.ReceivedProduct;
import ru.market.inventory_service.repository.ProductRepository;

@Mapper(componentModel = "spring",
        uses = {EmployeeRepository.class, ProductRepository.class})
public interface ReceivedProductMapper extends EntityMapper<ReceivedProduct, ReceivedProductDto> {

    @Mapping(target = "employeeId", source = "employee.employeeId")
    @Mapping(target = "productId",   source = "product.productId")
    ReceivedProductDto toDto(ReceivedProduct entity);

    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "employeeById")
    @Mapping(target = "product",   source = "productId",   qualifiedByName = "productById")
    ReceivedProduct toEntity(ReceivedProductDto dto);
}
