package ru.market.hr_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    @Id
    @GeneratedValue
    private Integer positionId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer monthlyHours;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal salaryRate;

}
