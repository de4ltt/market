package ru.market.hr_service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.market.hr_service.mapper.WorkDayMapper;
import ru.market.hr_service.model.dto.WorkDayDto;
import ru.market.hr_service.model.entity.Employee;
import ru.market.hr_service.model.entity.WorkDay;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.hr_service.repository.WorkDayRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeTrackingService {

    private final WorkDayRepository workDayRepository;
    private final EmployeeRepository employeeRepository;
    private final WorkDayMapper workDayMapper;

    /* ================= CHECK-IN ================= */

    public WorkDayDto checkIn(Integer employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if ("FIRED".equalsIgnoreCase(employee.getRole())) {
            throw new RuntimeException("Сотрудник уволен");
        }

        boolean hasActiveCheckIn =
                workDayRepository.existsByEmployeeEmployeeIdAndDateAndCheckInIsNotNull(
                        employeeId, LocalDate.now());

        if (hasActiveCheckIn) {
            throw new RuntimeException("У вас уже есть активный вход");
        }

        WorkDay workDay = workDayRepository
                .findByEmployeeEmployeeIdAndDate(employeeId, LocalDate.now())
                .orElseGet(() -> createNewWorkDay(employee));

        workDay.setCheckIn(LocalDateTime.now());
        workDay.setCheckOut(null);

        return workDayMapper.toDto(workDayRepository.save(workDay));
    }

    /* ================= CHECK-OUT ================= */

    public WorkDayDto checkOut(Integer employeeId) {

        WorkDay workDay = workDayRepository
                .findByEmployeeEmployeeIdAndDateAndCheckInIsNotNull(
                        employeeId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Нет активного входа"));

        LocalDateTime now = LocalDateTime.now();
        workDay.setCheckOut(now);

        addWorkedTime(workDay);
        recalcDayStats(workDay);

        return workDayMapper.toDto(workDayRepository.save(workDay));
    }


    private WorkDay createNewWorkDay(Employee employee) {
        WorkDay wd = new WorkDay();
        wd.setEmployee(employee);
        wd.setDate(LocalDate.now());
        wd.setHoursWorked(BigDecimal.ZERO);
        wd.setOvertime(BigDecimal.ZERO);
        wd.setUnderwork(BigDecimal.ZERO);
        return wd;
    }

    /**
     * Прибавляем время текущего входа к уже отработанному
     */
    private void addWorkedTime(WorkDay workDay) {

        Duration duration = Duration.between(
                workDay.getCheckIn(),
                workDay.getCheckOut()
        );

        BigDecimal hours = BigDecimal.valueOf(duration.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        workDay.setHoursWorked(
                workDay.getHoursWorked().add(hours)
        );
    }
    private void recalcDayStats(WorkDay workDay) {

        BigDecimal standard = BigDecimal.valueOf(8.0);
        BigDecimal tolerance = BigDecimal.valueOf(0.5);

        BigDecimal diff = workDay.getHoursWorked().subtract(standard);

        if (diff.abs().compareTo(tolerance) <= 0) {
            workDay.setOvertime(BigDecimal.ZERO);
            workDay.setUnderwork(BigDecimal.ZERO);
        } else if (diff.compareTo(BigDecimal.ZERO) > 0) {
            workDay.setOvertime(diff);
            workDay.setUnderwork(BigDecimal.ZERO);
        } else {
            workDay.setOvertime(BigDecimal.ZERO);
            workDay.setUnderwork(diff.abs());
        }
    }


    public List<WorkDayDto> getEmployeeWorkDays(
            Integer employeeId,
            LocalDate start,
            LocalDate end) {

        return workDayRepository
                .findByEmployeeEmployeeIdAndDateBetween(employeeId, start, end)
                .stream()
                .map(workDayMapper::toDto)
                .toList();
    }
}
