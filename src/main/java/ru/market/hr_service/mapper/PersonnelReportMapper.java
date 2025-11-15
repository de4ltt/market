package ru.market.hr_service.mapper;

import org.mapstruct.Mapper;
import ru.market.hr_service.model.dto.PersonnelReportDto;
import ru.market.hr_service.model.entity.PersonnelReport;

@Mapper(componentModel = "spring")
public interface PersonnelReportMapper {
    PersonnelReportDto toDto(PersonnelReport entity);
    PersonnelReport toEntity(PersonnelReportDto dto);
}
