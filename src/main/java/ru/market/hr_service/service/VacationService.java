package ru.market.hr_service.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.market.hr_service.mapper.VacationMapper;
import ru.market.hr_service.model.dto.VacationDto;
import ru.market.hr_service.model.entity.Vacation;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.hr_service.repository.VacationRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class VacationService {

    private static final int MAX_VACATION_DAYS_PER_YEAR = 28;
    private static final int SICK_LEAVE_CONFIRMATION_WINDOW_DAYS = 7;

    private final VacationRepository vacationRepository;
    private final EmployeeRepository employeeRepository;
    private final VacationMapper vacationMapper;

    /**
     * Создание запроса на отпуск/больничный
     * Для отпуска проверяется лимит 28 дней в календарном году
     */
    public VacationDto createVacationRequest(VacationDto vacationDto) {
        Vacation vacation = vacationMapper.toEntity(vacationDto);

        // Проверка, что сотрудник существует
        if (!employeeRepository.existsById(vacation.getEmployee().getEmployeeId())) {
            throw new IllegalArgumentException("Сотрудник не найден");
        }

        // Проверка лимита отпуска только для типа VACATION
        if ("VACATION".equals(vacation.getType())) {
            long daysRequested = ChronoUnit.DAYS.between(vacation.getStartDate(), vacation.getEndDate()) + 1;

            long alreadyUsedThisYear = calculateUsedVacationDaysThisYear(
                    vacation.getEmployee().getEmployeeId(),
                    vacation.getStartDate().getYear()
            );

            if (alreadyUsedThisYear + daysRequested > MAX_VACATION_DAYS_PER_YEAR) {
                throw new IllegalStateException(
                        String.format("Превышен годовой лимит отпуска (%d дней). Уже использовано: %d, запрашивается: %d",
                                MAX_VACATION_DAYS_PER_YEAR, alreadyUsedThisYear, daysRequested)
                );
            }
        }

        // Для больничных дополнительных проверок при создании обычно не делают
        Vacation saved = vacationRepository.save(vacation);
        return vacationMapper.toDto(saved);
    }

    /**
     * Подтверждение отпуска или больничного
     * Для больничных — проверка окна подтверждения (7 дней после окончания)
     */
    public VacationDto approveVacation(Integer vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));

        if (vacation.getApproved()) {
            throw new IllegalStateException("Заявка уже подтверждена");
        }

        // Проверка окна подтверждения для больничных
        if ("SICK_LEAVE".equals(vacation.getType())) {
            LocalDate lastDay = vacation.getEndDate();
            LocalDate deadline = lastDay.plusDays(SICK_LEAVE_CONFIRMATION_WINDOW_DAYS);

            if (LocalDate.now().isAfter(deadline)) {
                throw new IllegalStateException(
                        String.format("Срок подтверждения больничного истёк (%s). Крайний срок: %s",
                                LocalDate.now(), deadline)
                );
            }
        }

        // Для обычных отпусков ограничений по сроку подтверждения обычно нет

        vacation.setApproved(true);
        Vacation saved = vacationRepository.save(vacation);
        return vacationMapper.toDto(saved);
    }

    public List<VacationDto> getEmployeeVacations(Integer employeeId) {
        List<Vacation> vacations = vacationRepository.findByEmployeeEmployeeId(employeeId);
        return vacations.stream()
                .map(vacationMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<VacationDto> getPendingVacations() {
        List<Vacation> vacations = vacationRepository.findByApprovedFalse();
        return vacations.stream()
                .map(vacationMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Подсчёт всех дней отпуска в указанном году:
     * - подтверждённые (approved = true)
     * - ещё ожидающие подтверждения (approved = false)
     */
    private long calculateUsedVacationDaysThisYear(Integer employeeId, int year) {
        LocalDate yearStart = LocalDate.of(year, 1, 1);
        LocalDate yearEnd = LocalDate.of(year, 12, 31);

        // Ищем ВСЕ отпуска (подтверждённые и неподтверждённые)
        List<Vacation> allVacationsThisYear = vacationRepository
            .findByEmployeeEmployeeIdAndTypeAndStartDateBetween(
                employeeId, 
                "VACATION", 
                yearStart, 
                yearEnd
            );

        return allVacationsThisYear.stream()
            .filter(v -> Boolean.TRUE.equals(v.getApproved()) || Boolean.FALSE.equals(v.getApproved())) // все, кроме null или отклонённых
            .mapToLong(v -> ChronoUnit.DAYS.between(v.getStartDate(), v.getEndDate()) + 1)
            .sum();
    }
}