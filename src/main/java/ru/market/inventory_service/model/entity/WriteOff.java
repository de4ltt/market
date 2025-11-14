package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.market.hr_service.model.entity.Employee;

import java.time.LocalDate;

@Entity
@Data
public class WriteOff {

    @Id
    @GeneratedValue
    private Integer writeOffId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 100, nullable = false)
    private String writeOffReason;

    @Column
    private String comment;

}
