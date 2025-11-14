package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.inventory_service.model.dto.ReceivedProductDto;
import ru.market.inventory_service.model.entity.ReceivedProduct;
import ru.market.inventory_service.repository.ProductRepository;

@Mapper(componentModel = "spring", uses = {EmployeeRepository.class, ProductRepository.class})
interface ReceivedProductMapper {

    @Mapping(target = "employeeId", source = "employee.employeeId")
    @Mapping(target = "productId", source = "product.productId")
    ReceivedProductDto toDto(ReceivedProduct entity);

    @Mapping(target = "employee", source = "employeeId")
    @Mapping(target = "product", source = "productId")
    ReceivedProduct toEntity(ReceivedProductDto dto);

}
