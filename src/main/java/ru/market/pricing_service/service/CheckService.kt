package ru.market.pricing_service.service

import org.springframework.stereotype.Service
import ru.market.hr_service.repository.EmployeeRepository
import ru.market.pricing_service.model.dto.CheckDto
import ru.market.pricing_service.model.entity.Check
import ru.market.pricing_service.repository.CheckRepository

@Service
class CheckService(
    private val checkRepository: CheckRepository,
    private val employeeRepository: EmployeeRepository
){

    fun getAll(): List<CheckDto> =
        checkRepository.findAll().map { it.toDto() }

    fun getById(id: Int): CheckDto {
        val check = checkRepository.findById(id)
            .orElseThrow { NoSuchElementException("Check with id $id not found") }
        return check.toDto()
    }

    fun add(dto: CheckDto): CheckDto {
        val employee = employeeRepository.findById(dto.employeeId)
            .orElseThrow { NoSuchElementException("Employee with id ${dto.employeeId} not found") }

        val check = Check(
            checkId = 0,
            employee = employee,
            date = dto.date
        )
        return checkRepository.save(check).toDto()
    }

    fun updateById(id: Int, dto: CheckDto): CheckDto {
        val existing = checkRepository.findById(id)
            .orElseThrow { NoSuchElementException("Check with id $id not found") }

        val employee = employeeRepository.findById(dto.employeeId)
            .orElseThrow { NoSuchElementException("Employee with id ${dto.employeeId} not found") }

        val updated = existing.copy(
            employee = employee,
            date = dto.date
        )
        return checkRepository.save(updated).toDto()
    }

    fun deleteById(id: Int) {
        if (!checkRepository.existsById(id)) {
            throw NoSuchElementException("Check with id $id not found")
        }
        checkRepository.deleteById(id)
    }

    private fun Check.toDto(): CheckDto {
        return CheckDto(
            checkId = this.checkId,
            employeeId = this.employee.employeeId,
            date = this.date
        )
    }
}