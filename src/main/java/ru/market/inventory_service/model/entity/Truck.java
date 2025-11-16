package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.market.hr_service.model.entity.Employee;

import java.math.BigDecimal;

@Entity
@Table(name = "truck")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee driver;
}
