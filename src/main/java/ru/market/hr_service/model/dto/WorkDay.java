package ru.market.hr_service.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WorkDay {
    private Integer workDayId;
    private EmployeeDto employee;
    private LocalDate date;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private BigDecimal hoursWorked;
    private BigDecimal overtime;
    private BigDecimal underwork;
}