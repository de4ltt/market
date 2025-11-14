package ru.market.hr_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class PersonnelReport {

    @Id
    @GeneratedValue
    private Integer personnelReportId;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false, insertable = false, updatable = false)
    private Employee director;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(length = 50, nullable = false)
    private String status;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal totalHours = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal overtime = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal underwork = BigDecimal.ZERO;

    @Column
    private String comment;

}
