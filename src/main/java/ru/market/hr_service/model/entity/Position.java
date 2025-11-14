package ru.market.hr_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@Builder
public class Position {

    @Id
    @GeneratedValue
    private Integer positionId;

    @Column(length = 100)
    private String name;

    @Column
    private String description;

    @Column
    private Integer monthlyHours;

    @Column(precision = 10, scale = 2)
    private BigDecimal salaryRate;

}
