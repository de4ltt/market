package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class SupplyContract {

    @Id
    @GeneratedValue
    private Integer supplyContractId;

    @ManyToOne
    @JoinColumn(name = "counterparty_id", nullable = false)
    private Counterparty contractor;

    @ManyToOne
    @JoinColumn(name = "storage_location_id", nullable = false)
    private StorageLocation storageLocation;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

}
