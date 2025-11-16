package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supply_contract_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyContractItem {

    @Id
    @GeneratedValue
    private Integer supplyContractItemId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supply_contract_id", nullable = false)
    private SupplyContract supplyContract;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(length = 50, nullable = false)
    private String deliveryType;
}
