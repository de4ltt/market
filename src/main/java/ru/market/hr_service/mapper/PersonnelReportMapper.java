package ru.market.hr_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.hr_service.model.dto.PersonnelReportDto;
import ru.market.hr_service.model.entity.PersonnelReport;
import ru.market.hr_service.repository.EmployeeRepository;

@Mapper(componentModel = "spring", uses = {EmployeeRepository.class})
public interface PersonnelReportMapper {

    @Mapping(target = "directorId", source = "director.employeeId")
    @Mapping(target = "employeeId", source = "employee.employeeId")
    PersonnelReportDto toDto(PersonnelReport entity);

    @Mapping(target = "director", source = "employeeId", qualifiedByName = "employeeById")
    @Mapping(target = "employee", source = "employeeId", qualifiedByName = "employeeById")
    PersonnelReport toEntity(PersonnelReportDto dto);
}
