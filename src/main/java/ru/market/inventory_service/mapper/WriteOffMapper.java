package ru.market.inventory_service.mapper;

import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.inventory_service.model.dto.WriteOffDto;
import ru.market.inventory_service.model.entity.WriteOff;
import ru.market.inventory_service.repository.ProductRepository;

@Mapper(
        componentModel = "spring",
        uses = {EmployeeRepository.class, ProductRepository.class}
)
public interface WriteOffMapper {

    @Mapping(target = "employeeId", source = "employee.employeeId")
    @Mapping(target = "productId", source = "product.productId")
    WriteOffDto toDto(WriteOff entity);

    @Mapping(target = "employee", source = "employeeId")
    @Mapping(target = "product", source = "productId")
    WriteOff toEntity(WriteOffDto dto);

}
