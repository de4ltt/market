package ru.market.hr_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.hr_service.model.dto.VacationDto;
import ru.market.hr_service.model.entity.Vacation;
import ru.market.hr_service.repository.EmployeeRepository;

@Mapper(componentModel = "spring", uses = {EmployeeRepository.class})
public interface VacationMapper {

    @Mapping(target = "employeeId", source = "employee.employeeId")
    VacationDto toDto(Vacation entity);

    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "employeeById")
    Vacation toEntity(VacationDto dto);
}
