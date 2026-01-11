package ru.market.hr_service.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.market.hr_service.model.entity.Employee;
import ru.market.hr_service.model.entity.PersonnelReport;

@Repository
public interface PersonnelReportRepository extends JpaRepository<PersonnelReport, Integer> {
    List<PersonnelReport> findByEmployeeEmployeeId(Integer employeeId);
    List<PersonnelReport> findByDateBetween(LocalDate start, LocalDate end);
    // Проверка существования отчета черновика
    boolean existsByEmployeeAndDateAndStatus(Employee employee, LocalDate date, String status);

    // Проверка существования отчета
    boolean existsByEmployeeAndDate(Employee employee, LocalDate date);
    
    // Поиск по дате и статусу (для диапазона дат)
    List<PersonnelReport> findByDateBetweenAndStatus(
        LocalDate startDate, LocalDate endDate, String status);
    
    // Поиск по сотруднику и дате
    Optional<PersonnelReport> findByEmployeeAndDate(Employee employee, LocalDate date);
}
