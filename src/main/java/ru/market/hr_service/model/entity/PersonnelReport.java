package ru.market.hr_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Builder
public class PersonnelReport {

    @Id
    @GeneratedValue
    private Integer personnelReportId;

    @Column
    private LocalDate date;

    @ManyToOne
    private Employee director;

    @ManyToOne
    private Employee employee;

    @Column(length = 50)
    private String status;

    @Column(precision = 5, scale = 2)
    private BigDecimal totalHours;

    @Column(precision = 5, scale = 2)
    private BigDecimal overtime;

    @Column(precision = 5, scale = 2)
    private BigDecimal underwork;

    @Column
    private String comment;

}
