package ru.market.hr_service.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.hr_service.model.entity.Vacation;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Integer> {
    List<Vacation> findByEmployeeEmployeeId(Integer employeeId);
    List<Vacation> findByApprovedFalse();
    List<Vacation> findByStartDateBetween(LocalDate start, LocalDate end);
    List<Vacation> findByEmployeeEmployeeIdAndTypeAndStartDateBetween(
        Integer employeeId,
        String type,
        LocalDate start,
        LocalDate end
    );
}

