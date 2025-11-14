package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.market.hr_service.model.entity.Employee;

import java.math.BigDecimal;

@Entity
@Data
public class Truck {

    @Id
    @GeneratedValue
    private Integer truckId;

    @Column(length = 20, nullable = false)
    private String licencePlate;

    @Column(length = 100, nullable = false)
    private String model;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal capacity;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee driver;


}
