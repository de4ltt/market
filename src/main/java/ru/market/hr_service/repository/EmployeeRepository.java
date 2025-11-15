package ru.market.hr_service.repository;

import org.jetbrains.annotations.NotNull;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.hr_service.model.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @NotNull
    @Named("employeeById")
    Employee getReferenceById(@NotNull Integer id);
}
