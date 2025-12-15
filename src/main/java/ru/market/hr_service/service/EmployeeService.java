package ru.market.hr_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.market.hr_service.mapper.EmployeeMapper;
import ru.market.hr_service.model.dto.EmployeeDto;
import ru.market.hr_service.model.entity.Employee;
import ru.market.hr_service.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.toEntity(employeeDto);
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDto(saved);
    }
    
    public EmployeeDto getEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employeeMapper.toDto(employee);
    }
    
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
            .map(employeeMapper::toDto)
            .collect(Collectors.toList());
    }
    
    public EmployeeDto updateEmployee(Integer id, EmployeeDto employeeDto) {
        Employee existing = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        // Обновление всех полей
        existing.setFullName(employeeDto.getFullName());
        existing.setPassportSeries(employeeDto.getPassportSeries());
        existing.setPassportNumber(employeeDto.getPassportNumber());
        existing.setRegistrationAddress(employeeDto.getRegistrationAddress());
        existing.setBirthDate(employeeDto.getBirthDate());
        existing.setDepartment(employeeDto.getDepartment());
        existing.setLogin(employeeDto.getLogin());
        existing.setPassword(employeeDto.getPassword());
        existing.setRole(employeeDto.getRole());
        existing.setWorkPhone(employeeDto.getWorkPhone());
        existing.setPersonalPhone(employeeDto.getPersonalPhone());
        existing.setEmail(employeeDto.getEmail());
        
        Employee updated = employeeRepository.save(existing);
        return employeeMapper.toDto(updated);
    }
    public EmployeeDto patchEmployee(Integer id, Map<String, Object> updates) {
        Employee existing = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if (updates.containsKey("fullName") && updates.get("fullName") != null) {
            existing.setFullName((String) updates.get("fullName"));
        }
        
        if (updates.containsKey("passportSeries") && updates.get("passportSeries") != null) {
            existing.setPassportSeries((String) updates.get("passportSeries"));
        }
        
        if (updates.containsKey("passportNumber") && updates.get("passportNumber") != null) {
            existing.setPassportNumber((String) updates.get("passportNumber"));
        }
        
        if (updates.containsKey("registrationAddress") && updates.get("registrationAddress") != null) {
            existing.setRegistrationAddress((String) updates.get("registrationAddress"));
        }
        
        if (updates.containsKey("birthDate") && updates.get("birthDate") != null) {
            LocalDate birthDate = LocalDate.parse((String) updates.get("birthDate"));
            existing.setBirthDate(birthDate);
        }
        
        if (updates.containsKey("department") && updates.get("department") != null) {
            existing.setDepartment((String) updates.get("department"));
        }
        
        if (updates.containsKey("login") && updates.get("login") != null) {
            existing.setLogin((String) updates.get("login"));
        }
        
        if (updates.containsKey("password") && updates.get("password") != null) {
            existing.setPassword((String) updates.get("password"));
        }
        
        if (updates.containsKey("role") && updates.get("role") != null) {
            existing.setRole((String) updates.get("role"));
        }
        
        if (updates.containsKey("workPhone") && updates.get("workPhone") != null) {
            existing.setWorkPhone((String) updates.get("workPhone"));
        }
        
        if (updates.containsKey("personalPhone") && updates.get("personalPhone") != null) {
            existing.setPersonalPhone((String) updates.get("personalPhone"));
        }
        
        if (updates.containsKey("email") && updates.get("email") != null) {
            existing.setEmail((String) updates.get("email"));
        }
        
        Employee updated = employeeRepository.save(existing);
        return employeeMapper.toDto(updated);
    }
    
public EmployeeDto dismissEmployee(Integer employeeId, Integer directorId) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new RuntimeException("Employee not found"));

    if ("FIRED".equalsIgnoreCase(employee.getRole())) {
        throw new RuntimeException("Сотрудник уже уволен");
    }
    employee.setRole("FIRED");
    Employee saved = employeeRepository.save(employee);
    return employeeMapper.toDto(saved);
}
    
    public EmployeeDto changePassword(Integer id, String newPassword) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setPassword(newPassword);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toDto(updated);
    }
    
    public EmployeeDto changeDepartment(Integer id, String newDepartment) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setDepartment(newDepartment);
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toDto(updated);
    }
    
    public EmployeeDto changePosition(Integer id, Integer newPositionId) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        Employee updated = employeeRepository.save(employee);
        return employeeMapper.toDto(updated);
    }
}