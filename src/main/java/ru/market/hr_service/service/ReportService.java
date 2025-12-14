package ru.market.hr_service.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.market.hr_service.mapper.PersonnelReportMapper;
import ru.market.hr_service.model.dto.PersonnelReportDto;
import ru.market.hr_service.model.entity.PersonnelReport;
import ru.market.hr_service.model.entity.WorkDay;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.hr_service.repository.PersonnelReportRepository;
import ru.market.hr_service.repository.WorkDayRepository;

@Service
@Transactional
public class ReportService {

    @Autowired
    private PersonnelReportRepository reportRepository;

    @Autowired
    private WorkDayRepository workDayRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PersonnelReportMapper reportMapper;

    // Генерация черновика отчёта за неделю
    public PersonnelReportDto generateWeeklyDraft(Integer employeeId, Integer directorId) {
        LocalDate weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        List<WorkDay> workDays = workDayRepository
            .findByEmployeeEmployeeIdAndDateBetween(employeeId, weekStart, weekEnd);

        BigDecimal totalHours = workDays.stream()
            .map(WorkDay::getHoursWorked)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOvertime = workDays.stream()
            .map(WorkDay::getOvertime)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalUnderwork = workDays.stream()
            .map(WorkDay::getUnderwork)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Проверка — может уже есть черновик?
        boolean exists = reportRepository.findByEmployeeEmployeeId(employeeId).stream()
            .anyMatch(r -> r.getDate().equals(weekEnd) && "DRAFT".equals(r.getStatus()));

        if (exists) {
            throw new RuntimeException("Черновик за эту неделю уже создан");
        }

        PersonnelReport report = new PersonnelReport();
        report.setDate(weekEnd);
        report.setEmployee(employeeRepository.getReferenceById(employeeId));
        report.setDirector(employeeRepository.getReferenceById(directorId));
        report.setStatus("DRAFT");
        report.setTotalHours(totalHours);
        report.setOvertime(totalOvertime);
        report.setUnderwork(totalUnderwork);
        report.setComment("Черновик отчёта за неделю " + weekStart + " по " + weekEnd);

        PersonnelReport saved = reportRepository.save(report);
        return reportMapper.toDto(saved);
    }

    // Подтверждение отчёта директором с возможностью корректировки
    public PersonnelReportDto confirmWeeklyReport(
            Integer reportId,
            Integer directorId,
            String directorComment,
            BigDecimal manualOvertime,
            BigDecimal manualUnderwork) {

        PersonnelReport report = reportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Отчёт не найден"));

        // if (!report.getDirector().getEmployeeId().equals(directorId)) {
        //     throw new RuntimeException("Только директор может подтвердить отчёт");
        // }

        if (!"DRAFT".equals(report.getStatus())) {
            throw new RuntimeException("Отчёт уже подтверждён или отправлен");
        }

        if (manualOvertime != null) report.setOvertime(manualOvertime);
        if (manualUnderwork != null) report.setUnderwork(manualUnderwork);

        report.setComment(directorComment != null ? directorComment : report.getComment());
        report.setStatus("CONFIRMED"); // или "SENT_TO_HQ"

        PersonnelReport saved = reportRepository.save(report);
        return reportMapper.toDto(saved);
    }

    public List<PersonnelReportDto> getReportsByEmployee(Integer employeeId) {
        return reportRepository.findByEmployeeEmployeeId(employeeId).stream()
            .map(reportMapper::toDto)
            .collect(Collectors.toList());
    }
}