package ru.market.hr_service.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.market.hr_service.model.dto.PersonnelReportDto;
import ru.market.hr_service.service.ReportService;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfirmReportRequest {
        private String directorComment;
        private BigDecimal manualOvertime;   // может быть null — значит не меняем
        private BigDecimal manualUnderwork;  // может быть null — значит не меняем
    }

    // Создать черновик отчёта за неделю
    @PostMapping("/weekly/draft")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<PersonnelReportDto> createWeeklyDraft(
            @RequestParam Integer employeeId,
            @RequestParam Integer directorId) {
        return ResponseEntity.ok(reportService.generateWeeklyDraft(employeeId, directorId));
    }

    // Подтвердить отчёт (с корректировкой)
    @PostMapping("/{reportId}/confirm")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<PersonnelReportDto> confirmReport(
            @PathVariable Integer reportId,
            @RequestParam Integer directorId,
            @RequestBody ConfirmReportRequest request) {

        PersonnelReportDto confirmed = reportService.confirmWeeklyReport(
            reportId,
            directorId,
            request.getDirectorComment(),
            request.getManualOvertime(),
            request.getManualUnderwork()
        );
        return ResponseEntity.ok(confirmed);
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<List<PersonnelReportDto>> getEmployeeReports(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(reportService.getReportsByEmployee(employeeId));
    }
}