package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SupplyContractItem {

    @Id
    @GeneratedValue
    private Integer supplyContractItemId;

    @ManyToOne
    @JoinColumn(name = "supply_contract_id", nullable = false)
    private SupplyContract supplyContract;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 50, nullable = false)
    private String deliveryType;

}
