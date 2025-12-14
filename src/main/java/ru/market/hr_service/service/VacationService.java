package ru.market.hr_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.market.hr_service.mapper.VacationMapper;
import ru.market.hr_service.model.dto.VacationDto;
import ru.market.hr_service.model.entity.Vacation;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.hr_service.repository.VacationRepository;

@Service
@Transactional
public class VacationService {
    
    @Autowired
    private VacationRepository vacationRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private VacationMapper vacationMapper;
    
    public VacationDto createVacationRequest(VacationDto vacationDto) {
        Vacation vacation = vacationMapper.toEntity(vacationDto);
        Vacation saved = vacationRepository.save(vacation);
        return vacationMapper.toDto(saved);
    }
    
    public VacationDto approveVacation(Integer vacationId) {
        Vacation vacation = vacationRepository.findById(vacationId)
            .orElseThrow(() -> new RuntimeException("Vacation not found"));
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
}