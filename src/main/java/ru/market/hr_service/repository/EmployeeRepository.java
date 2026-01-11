package ru.market.hr_service.repository;

import java.util.List;
import java.util.Optional;

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

    // Находим всех сотрудников не с ролью FIRED
    List<Employee> findByRoleNot(String role);
    
    // Находим по роли
    List<Employee> findByRole(String role);
        
    // Находим первого директора
    Optional<Employee> findFirstByRole(String role);
}
