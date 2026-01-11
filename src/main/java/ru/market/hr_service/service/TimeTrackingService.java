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

    private static final BigDecimal STANDARD_DAY_HOURS = new BigDecimal("8.00");
    private static final BigDecimal LUNCH_BREAK_HOURS = new BigDecimal("1.00");
    private static final BigDecimal TOLERANCE_MINUTES = new BigDecimal("30");
    private static final BigDecimal MINUTES_IN_HOUR = new BigDecimal("60");

    /* ================= CHECK-IN ================= */
    public WorkDayDto checkIn(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Сотрудник не найден"));

        if ("FIRED".equalsIgnoreCase(employee.getRole())) {
            throw new RuntimeException("Сотрудник уволен");
        }

        LocalDate today = LocalDate.now();

        // Проверяем, был ли уже check-in сегодня
        boolean alreadyCheckedIn = workDayRepository
                .existsByEmployeeEmployeeIdAndDateAndCheckInIsNotNull(employeeId, today);

        if (alreadyCheckedIn) {
            throw new RuntimeException("Вы уже отмечались сегодня (check-in выполнен)");
        }

        WorkDay workDay = workDayRepository
                .findByEmployeeEmployeeIdAndDate(employeeId, today)
                .orElseGet(() -> createNewWorkDay(employee));

        workDay.setCheckIn(LocalDateTime.now());
        // checkOut остаётся null

        return workDayMapper.toDto(workDayRepository.save(workDay));
    }

    /* ================= CHECK-OUT ================= */
    public WorkDayDto checkOut(Integer employeeId) {
        LocalDate today = LocalDate.now();

        WorkDay workDay = workDayRepository
                .findByEmployeeEmployeeIdAndDateAndCheckInIsNotNullAndCheckOutIsNull(
                        employeeId, today)
                .orElseThrow(() -> new RuntimeException("Нет активного check-in на сегодня"));

        if (workDay.getCheckOut() != null) {
            throw new RuntimeException("Check-out уже выполнен сегодня");
        }

        LocalDateTime now = LocalDateTime.now();
        workDay.setCheckOut(now);

        calculateWorkedTimeAndStats(workDay);

        return workDayMapper.toDto(workDayRepository.save(workDay));
    }
    
    public WorkDayDto checkoutWithZeroOvertime(Integer employeeId) {
        LocalDate today = LocalDate.now();

        WorkDay workDay = workDayRepository
            .findByEmployeeEmployeeIdAndDateAndCheckInIsNotNullAndCheckOutIsNull(employeeId, today)
            .orElseThrow(() -> new RuntimeException("Нет активного check-in на сегодня"));

        LocalDateTime now = LocalDateTime.now();
        workDay.setCheckOut(now);

        // Обычный расчёт отработанного времени (с учётом обеда и допуска ±30 мин)
        calculateWorkedTimeAndStats(workDay);

        // Главное — обнуляем переработку
        workDay.setOvertime(BigDecimal.ZERO);

        workDayRepository.save(workDay);

        return workDayMapper.toDto(workDay);
    }

    private void calculateWorkedTimeAndStats(WorkDay workDay) {
        if (workDay.getCheckIn() == null || workDay.getCheckOut() == null) {
            return;
        }

        Duration presence = Duration.between(workDay.getCheckIn(), workDay.getCheckOut());

        // Переводим в часы с двумя знаками после запятой
        BigDecimal presenceHours = BigDecimal.valueOf(presence.toMinutes())
                .divide(MINUTES_IN_HOUR, 2, RoundingMode.HALF_UP);

        // Вычитаем обед (1 час)
        BigDecimal effectiveHours = presenceHours.subtract(LUNCH_BREAK_HOURS);

        // Если после вычета обеда получилось меньше 0 — ставим 0
        if (effectiveHours.compareTo(BigDecimal.ZERO) < 0) {
            effectiveHours = BigDecimal.ZERO;
        }

        workDay.setHoursWorked(effectiveHours);

        // Расчёт переработки / недоработки с учётом допуска ±30 минут
        BigDecimal diff = effectiveHours.subtract(STANDARD_DAY_HOURS);

        // Допуск ±0.5 часа (30 минут)
        if (diff.abs().compareTo(TOLERANCE_MINUTES.divide(MINUTES_IN_HOUR, 2, RoundingMode.HALF_UP)) <= 0) {
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

    private WorkDay createNewWorkDay(Employee employee) {
        WorkDay wd = new WorkDay();
        wd.setEmployee(employee);
        wd.setDate(LocalDate.now());
        wd.setHoursWorked(BigDecimal.ZERO);
        wd.setOvertime(BigDecimal.ZERO);
        wd.setUnderwork(BigDecimal.ZERO);
        return wd;
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