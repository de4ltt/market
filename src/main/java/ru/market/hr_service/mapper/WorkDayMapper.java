package ru.market.hr_service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.market.hr_service.model.dto.WorkDayDto;
import ru.market.hr_service.model.entity.Employee;
import ru.market.hr_service.model.entity.WorkDay;
import ru.market.hr_service.repository.EmployeeRepository;

@Component
@RequiredArgsConstructor
public class WorkDayMapper {
    
    private final EmployeeRepository employeeRepository;
    
    public WorkDayDto toDto(WorkDay entity) {
        if (entity == null) {
            return null;
        }
        
        WorkDayDto dto = new WorkDayDto();
        dto.setWorkDayId(entity.getWorkDayId());
        dto.setEmployeeId(entity.getEmployee() != null ? entity.getEmployee().getEmployeeId() : null);
        dto.setDate(entity.getDate());
        dto.setCheckIn(entity.getCheckIn());
        dto.setCheckOut(entity.getCheckOut());
        dto.setHoursWorked(entity.getHoursWorked());
        dto.setOvertime(entity.getOvertime());
        dto.setUnderwork(entity.getUnderwork());
        
        return dto;
    }
    
    public WorkDay toEntity(WorkDayDto dto) {
        if (dto == null) {
            return null;
        }
        
        WorkDay entity = new WorkDay();
        entity.setWorkDayId(dto.getWorkDayId());
        entity.setDate(dto.getDate());
        entity.setCheckIn(dto.getCheckIn());
        entity.setCheckOut(dto.getCheckOut());
        entity.setHoursWorked(dto.getHoursWorked());
        entity.setOvertime(dto.getOvertime());
        entity.setUnderwork(dto.getUnderwork());
        
        if (dto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + dto.getEmployeeId()));
            entity.setEmployee(employee);
        }
        
        return entity;
    }
}