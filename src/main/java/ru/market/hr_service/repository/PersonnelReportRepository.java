package ru.market.hr_service.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.hr_service.model.entity.PersonnelReport;

@Repository
public interface PersonnelReportRepository extends JpaRepository<PersonnelReport, Integer> {
    List<PersonnelReport> findByEmployeeEmployeeId(Integer employeeId);
    List<PersonnelReport> findByDateBetween(LocalDate start, LocalDate end);
}
