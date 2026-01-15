package ru.market.pricing_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.hr_service.model.entity.Employee;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.pricing_service.model.dto.CheckDto;
import ru.market.pricing_service.model.entity.Check;
import ru.market.pricing_service.repository.CheckRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckService {

    private final CheckRepository checkRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public CheckService(CheckRepository checkRepository, EmployeeRepository employeeRepository) {
        this.checkRepository = checkRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<CheckDto> getAll() {
        return checkRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public CheckDto getById(Integer id) {
        Check check = checkRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Check with id " + id + " not found"));
        return toDto(check);
    }

    public CheckDto add(CheckDto dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
            .orElseThrow(() -> new EntityNotFoundException("Employee with id " + dto.getEmployeeId() + " not found"));

        Check check = new Check();
        check.setEmployee(employee);
        check.setDate(dto.getDate());

        return toDto(checkRepository.save(check));
    }

    public CheckDto updateById(Integer id, CheckDto dto) {
        Check existing = checkRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Check with id " + id + " not found"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
            .orElseThrow(() -> new EntityNotFoundException("Employee with id " + dto.getEmployeeId() + " not found"));

        existing.setEmployee(employee);
        existing.setDate(dto.getDate());

        return toDto(checkRepository.save(existing));
    }

    public void deleteById(Integer id) {
        if (!checkRepository.existsById(id)) {
            throw new EntityNotFoundException("Check with id " + id + " not found");
        }
        checkRepository.deleteById(id);
    }

    private CheckDto toDto(Check entity) {
        return new CheckDto(
                entity.getCheckId(),
        entity.getEmployee().getEmployeeId(),
        entity.getDate()
        );
    }
}