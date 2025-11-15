package ru.market.hr_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.hr_service.model.dto.EmployeeDto;
import ru.market.hr_service.model.entity.Employee;
import ru.market.hr_service.repository.PositionRepository;

@Mapper(componentModel = "spring", uses = {PositionRepository.class})
public interface EmployeeMapper {

    @Mapping(target = "positionId", source = "position.positionId")
    EmployeeDto toDto(Employee entity);

    @Mapping(target = "position", source = "positionId", qualifiedByName = "positionById")
    Employee toEntity(EmployeeDto dto);
}
