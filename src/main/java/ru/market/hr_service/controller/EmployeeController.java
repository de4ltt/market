package ru.market.hr_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.market.hr_service.model.dto.EmployeeDto;
import ru.market.hr_service.service.EmployeeService;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    @PostMapping
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto created = employeeService.createEmployee(employeeDto);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DIRECTOR')")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Integer id) {
        EmployeeDto employee = employeeService.getEmployee(id);
        return ResponseEntity.ok(employee);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('DIRECTOR')")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<EmployeeDto> patchEmployee(
            @PathVariable Integer id, 
            @RequestBody Map<String, Object> updates) {
        EmployeeDto updated = employeeService.patchEmployee(id, updates);
        return ResponseEntity.ok(updated);
    }
    
    @PostMapping("/{id}/dismiss")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<EmployeeDto> dismissEmployee(
            @PathVariable Integer id,
            @RequestParam Integer directorId) {
        EmployeeDto dismissed = employeeService.dismissEmployee(id, directorId);
        return ResponseEntity.ok(dismissed);
    }
}