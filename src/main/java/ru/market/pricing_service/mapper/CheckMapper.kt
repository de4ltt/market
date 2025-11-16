package ru.market.pricing_service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.market.hr_service.repository.EmployeeRepository
import ru.market.pricing_service.model.dto.CheckDto
import ru.market.pricing_service.model.entity.Check

@Mapper(componentModel = "spring", uses = [EmployeeRepository::class])
interface CheckMapper {

    @Mapping(target = "employeeId", source = "employee.employeeId")
    fun toDto(entity: Check): CheckDto

    @Mapping(target = "employee", source = "employeeId", qualifiedByName = ["employeeById"])
    fun toEntity(dto: CheckDto): Check
}