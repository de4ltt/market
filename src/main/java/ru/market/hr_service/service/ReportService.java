package ru.market.hr_service.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.market.hr_service.mapper.PersonnelReportMapper;
import ru.market.hr_service.model.dto.PersonnelReportDto;
import ru.market.hr_service.model.entity.Employee;
import ru.market.hr_service.model.entity.PersonnelReport;
import ru.market.hr_service.model.entity.WorkDay;
import ru.market.hr_service.model.entity.WorkScheduleTemplate;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.hr_service.repository.PersonnelReportRepository;
import ru.market.hr_service.repository.WorkDayRepository;

@Service
@Transactional
public class ReportService {

    private final PersonnelReportRepository reportRepository;
    private final WorkDayRepository workDayRepository;
    private final EmployeeRepository employeeRepository;
    private final PersonnelReportMapper reportMapper;

    public ReportService(
            PersonnelReportRepository reportRepository,
            WorkDayRepository workDayRepository,
            EmployeeRepository employeeRepository,
            PersonnelReportMapper reportMapper) {
        this.reportRepository = reportRepository;
        this.workDayRepository = workDayRepository;
        this.employeeRepository = employeeRepository;
        this.reportMapper = reportMapper;
    }

    // Генерация отчётов для всех активных сотрудников
    public String generateWeeklyReportsForAll(Integer directorId) {
        LocalDate weekStart = getCurrentWeekStart();
        LocalDate weekEnd = getCurrentWeekEnd();

        List<Employee> activeEmployees = employeeRepository.findByRoleNot("FIRED");

        if (activeEmployees.isEmpty()) {
            throw new RuntimeException("Нет активных сотрудников для генерации отчётов");
        }

        int generatedCount = 0;

        for (Employee employee : activeEmployees) {
            if (reportRepository.existsByEmployeeAndDateAndStatus(employee, weekEnd, "DRAFT")) {
                continue;
            }

            PersonnelReport report = createReportForEmployee(employee, directorId, weekStart, weekEnd);
            if (report != null) {
                reportRepository.save(report);
                generatedCount++;
            }
        }

        return String.format("Успешно создано %d отчётов за период %s - %s",
                generatedCount, weekStart, weekEnd);
    }

    // Получение текущей недели (с автогенерацией при необходимости)
    public List<PersonnelReportDto> getCurrentWeekReports() {
        LocalDate weekStart = getCurrentWeekStart();
        LocalDate weekEnd = getCurrentWeekEnd();

        List<PersonnelReport> reports = reportRepository
                .findByDateBetween(weekStart, weekEnd);

        if (reports.isEmpty()) {
            generateWeeklyReportsAutomatically();
        }
        reports = reportRepository.findByDateBetweenAndStatus(weekStart, weekEnd, "DRAFT");

        return reportMapper.toDtoList(reports);
    }

    @Scheduled(cron = "0 0 0 * * MON")  // каждый понедельник в 00:00
    @Transactional
    public void generateWeeklyReportsAutomatically() {
        Employee director = employeeRepository.findFirstByRole("ROLE_DIRECTOR")
                .orElseThrow(() -> new RuntimeException("Директор не найден"));

        generateWeeklyReportsForAll(director.getEmployeeId());
    }

    private PersonnelReport createReportForEmployee(Employee employee, Integer directorId,
                                                LocalDate weekStart, LocalDate weekEnd) {
        try {
            // 1. Берём график сотрудника напрямую
            WorkScheduleTemplate template = employee.getTemplate();

            if (template == null) {
                throw new RuntimeException("У сотрудника " + employee.getEmployeeId() + " не назначен график работы");
            }

            // 2. Рабочие дни по графику
            Set<DayOfWeek> workingDaysOfWeek = template.getWorkingDays();

            // 3. Все дни недели
            List<LocalDate> allWeekDays = getAllDaysInWeek(weekStart, weekEnd);

            // 4. Рабочие дни сотрудника в этой неделе
            List<LocalDate> requiredDays = allWeekDays.stream()
                    .filter(date -> workingDaysOfWeek.contains(date.getDayOfWeek()))
                    .collect(Collectors.toList());

            // 5. Фактические записи WorkDay
            List<WorkDay> workDays = workDayRepository.findByEmployeeEmployeeIdAndDateBetween(
                    employee.getEmployeeId(), weekStart, weekEnd);

            // 6. Сумма отработанного
            BigDecimal totalWorked = sumField(workDays, WorkDay::getHoursWorked);
            BigDecimal overtimeFromDays = sumField(workDays, WorkDay::getOvertime);
            BigDecimal underworkFromDays = sumField(workDays, WorkDay::getUnderwork);

            // 7. Недоработка за пропущенные рабочие дни
            BigDecimal absentUnderwork = BigDecimal.ZERO;
            List<String> absentDaysList = new ArrayList<>();

            for (LocalDate day : requiredDays) {
                boolean hasRecord = workDays.stream()
                        .anyMatch(wd -> wd.getDate().equals(day));

                if (!hasRecord) {
                    absentUnderwork = absentUnderwork.add(BigDecimal.valueOf(8.00));
                    absentDaysList.add(day.getDayOfWeek().name().substring(0, 3).toUpperCase());
                }
            }

            BigDecimal finalOvertime = overtimeFromDays;
            BigDecimal finalUnderwork = underworkFromDays.add(absentUnderwork);

            // 8. Комментарий
            StringBuilder comment = new StringBuilder();
            comment.append("Недельный отчёт ").append(weekStart).append(" – ").append(weekEnd);
            comment.append("\nГрафик: ").append(template.getName());

            if (!absentDaysList.isEmpty()) {
                comment.append("\n\nПропущенные рабочие дни по графику (+8 ч недоработки каждый): ")
                    .append(String.join(", ", absentDaysList));
            }

            if (finalOvertime.compareTo(BigDecimal.ZERO) > 0) {
                comment.append("\n\nПереработка: ").append(finalOvertime.setScale(2));
            }
            if (finalUnderwork.compareTo(BigDecimal.ZERO) > 0) {
                comment.append("\nНедоработка: ").append(finalUnderwork.setScale(2));
            }

            // 9. Создаём отчёт
            PersonnelReport report = new PersonnelReport();
            report.setDate(weekEnd);
            report.setEmployee(employee);
            report.setDirector(employeeRepository.getReferenceById(directorId));
            report.setStatus("DRAFT");
            report.setTotalHours(totalWorked);
            report.setOvertime(finalOvertime);
            report.setUnderwork(finalUnderwork);
            report.setComment(comment.toString().trim());

            return report;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании отчёта для сотрудника " + employee.getEmployeeId(), e);
        }
    }

    // ────────────────────────────────────────────────────────────────────────────────
    // Вспомогательные методы
    // ────────────────────────────────────────────────────────────────────────────────

    private BigDecimal sumField(List<WorkDay> workDays, Function<WorkDay, BigDecimal> mapper) {
        return workDays.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<LocalDate> getAllDaysInWeek(LocalDate start, LocalDate end) {
        List<LocalDate> days = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private LocalDate getCurrentWeekStart() {
        return LocalDate.now().with(DayOfWeek.MONDAY);
    }

    private LocalDate getCurrentWeekEnd() {
        return getCurrentWeekStart().plusDays(6);
    }

    // Изменение отельного отчета о сотруднике 
    public PersonnelReportDto confirmWeeklyReport(
            Integer reportId,
            Integer directorId,
            String directorComment,
            BigDecimal manualOvertime,
            BigDecimal manualUnderwork) {

        PersonnelReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Отчёт не найден"));

        if (manualOvertime != null) {
            report.setOvertime(manualOvertime);
        }
        if (manualUnderwork != null) {
            report.setUnderwork(manualUnderwork);
        }

        if (directorComment != null && !directorComment.isBlank()) {
            report.setComment(directorComment);
        }

        return reportMapper.toDto(reportRepository.save(report));
    }

    public List<PersonnelReportDto> getReportsByEmployee(Integer employeeId) {
        return reportRepository.findByEmployeeEmployeeId(employeeId).stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }
    /**
     * Подтверждает ВСЕ отчёты текущей недели (по всем сотрудникам),
     * которые находятся в статусе DRAFT
     */
    @Transactional
    public void confirmAllCurrentWeekReports(Integer directorId) {
        LocalDate weekStart = getCurrentWeekStart();
        LocalDate weekEnd = getCurrentWeekEnd();

        // Находим все черновики за текущую неделю
        List<PersonnelReport> drafts = reportRepository
                .findByDateBetweenAndStatus(weekStart, weekEnd, "DRAFT");

        if (drafts.isEmpty()) {
            return; // или можно бросить исключение/вернуть сообщение
        }

        for (PersonnelReport report : drafts) {
            report.setStatus("CONFIRMED");
        }

        reportRepository.saveAll(drafts);
    }
}