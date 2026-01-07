package ru.market.hr_service.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.market.hr_service.model.dto.WorkDayDto;
import ru.market.hr_service.service.TimeTrackingService;

@RestController
@RequestMapping("/time-tracking")
@RequiredArgsConstructor
public class TimeTrackingController {
    
    private final TimeTrackingService timeTrackingService;
    
    @PostMapping("/{employeeId}/check-in")
    public ResponseEntity<WorkDayDto> checkIn(@PathVariable Integer employeeId) {
        WorkDayDto workDay = timeTrackingService.checkIn(employeeId);
        return ResponseEntity.ok(workDay);
    }
    
    @PostMapping("/{employeeId}/check-out")
    public ResponseEntity<WorkDayDto> checkOut(@PathVariable Integer employeeId) {
        WorkDayDto workDay = timeTrackingService.checkOut(employeeId);
        return ResponseEntity.ok(workDay);
    }
    
    @GetMapping("/{employeeId}/work-days")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<List<WorkDayDto>> getWorkDays(
            @PathVariable Integer employeeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<WorkDayDto> workDays = timeTrackingService.getEmployeeWorkDays(employeeId, startDate, endDate);
        return ResponseEntity.ok(workDays);
    }
}