package ru.market.hr_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Builder
public class Vacation {

    @Id
    @GeneratedValue
    private Integer vacationId;

    @ManyToOne
    private Employee employee;

    @Column(length = 50)
    private String type;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    private Boolean approved;

}
