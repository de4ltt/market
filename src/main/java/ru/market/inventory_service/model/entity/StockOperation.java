package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.market.hr_service.model.entity.Employee;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockOperation {

    @Id
    @GeneratedValue
    private Integer stockOperationId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee responsibleEmployee;

    @ManyToOne
    @JoinColumn(name = "storage_location_id", nullable = false)
    private StorageLocation storageLocation;

    @ManyToOne
    @JoinColumn(name = "shelf_id", nullable = false)
    private Shelf shelf;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(length = 50, nullable = false)
    private String operationType;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDate timestamp;

}