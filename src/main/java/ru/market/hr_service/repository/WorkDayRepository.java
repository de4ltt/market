package ru.market.hr_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.hr_service.model.entity.WorkDay;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Integer> {
}
