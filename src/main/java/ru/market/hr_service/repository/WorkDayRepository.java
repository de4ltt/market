package ru.market.hr_service.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.hr_service.model.entity.WorkDay;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Integer> {

    Optional<WorkDay> findByEmployeeEmployeeIdAndDate(Integer employeeId, LocalDate date);

    Optional<WorkDay> findByEmployeeEmployeeIdAndDateAndCheckInIsNotNull(Integer employeeId, LocalDate date);

    boolean existsByEmployeeEmployeeIdAndDateAndCheckInIsNotNull(Integer employeeId, LocalDate date);

    List<WorkDay> findByEmployeeEmployeeIdAndDateBetween(
        Integer employeeId, LocalDate start, LocalDate end);

    boolean existsByEmployeeEmployeeIdAndDateAndCheckInIsNotNullAndCheckOutIsNull(
        Integer employeeId, LocalDate date);
        
    // Для check-out
    Optional<WorkDay> findByEmployeeEmployeeIdAndDateAndCheckInIsNotNullAndCheckOutIsNull(
        Integer employeeId, LocalDate date);
}
