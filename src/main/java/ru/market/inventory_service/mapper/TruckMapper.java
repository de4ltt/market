package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.inventory_service.model.dto.TruckDto;
import ru.market.inventory_service.model.entity.Truck;

@Mapper(componentModel = "spring", uses = {EmployeeRepository.class})
public interface TruckMapper extends EntityMapper<Truck, TruckDto> {

    @Mapping(target = "driverId", source = "driver.employeeId")
    TruckDto toDto(Truck entity);

    @Mapping(target = "driver", source = "driverId", qualifiedByName = "employeeById")
    Truck toEntity(TruckDto dto);
}
