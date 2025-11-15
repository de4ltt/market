package ru.market.hr_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_day")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDay {

    @Id
    @GeneratedValue
    private Integer workDayId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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
