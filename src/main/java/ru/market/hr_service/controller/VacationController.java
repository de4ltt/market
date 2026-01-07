package ru.market.hr_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.market.hr_service.model.dto.VacationDto;
import ru.market.hr_service.service.VacationService;

@RestController
@RequestMapping("/vacations")
@RequiredArgsConstructor
public class VacationController {
    
    private final VacationService vacationService;
    
    @PostMapping
    public ResponseEntity<VacationDto> createVacation(@RequestBody VacationDto vacationDto) {
        VacationDto created = vacationService.createVacationRequest(vacationDto);
        return ResponseEntity.ok(created);
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<VacationDto> approveVacation(@PathVariable Integer id) {
        VacationDto approved = vacationService.approveVacation(id);
        return ResponseEntity.ok(approved);
    }
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<VacationDto>> getEmployeeVacations(@PathVariable Integer employeeId) {
        List<VacationDto> vacations = vacationService.getEmployeeVacations(employeeId);
        return ResponseEntity.ok(vacations);
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<List<VacationDto>> getPendingVacations() {
        List<VacationDto> vacations = vacationService.getPendingVacations();
        return ResponseEntity.ok(vacations);
    }
}