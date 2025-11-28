package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.inventory_service.model.dto.ReceivedProductDto;
import ru.market.inventory_service.model.entity.ReceivedProduct;

@Mapper(componentModel = "spring",
        uses = {EmployeeRepository.class, ProductMapper.class})
public interface ReceivedProductMapper extends EntityMapper<ReceivedProduct, ReceivedProductDto> {

    @Mapping(target = "employeeId", source = "employee.employeeId")
    ReceivedProductDto toDto(ReceivedProduct entity);

    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "employeeById")
    ReceivedProduct toEntity(ReceivedProductDto dto);
}
