package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "supply_contract")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyContract {

    @Id
    @GeneratedValue
    private Integer supplyContractId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "counterparty_id", nullable = false)
    private Counterparty contractor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "storage_location_id", nullable = false)
    private StorageLocation storageLocation;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;
}
