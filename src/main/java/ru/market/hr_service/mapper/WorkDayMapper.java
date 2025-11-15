package ru.market.hr_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.hr_service.model.dto.WorkDayDto;
import ru.market.hr_service.model.entity.WorkDay;
import ru.market.hr_service.repository.EmployeeRepository;

@Mapper(componentModel = "spring", uses = {EmployeeRepository.class})
public interface WorkDayMapper{

    @Mapping(target = "employeeId", source = "employee.employeeId")
    WorkDayDto toDto(WorkDay entity);

    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "employeeById")
    WorkDay toEntity(WorkDayDto dto);
}
