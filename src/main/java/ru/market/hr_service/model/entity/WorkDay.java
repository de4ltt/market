package ru.market.hr_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class WorkDay {

    @Id
    @GeneratedValue
    private Integer workDayId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column
    private LocalDate date;

    @Column
    private LocalDateTime checkIn;

    @Column
    private LocalDateTime checkOut;

    @Column(precision = 5, scale = 2)
    private BigDecimal hoursWorked;

    @Column(precision = 5, scale = 2)
    private BigDecimal overtime;

    @Column(precision = 5, scale = 2)
    private BigDecimal underwork;

}
