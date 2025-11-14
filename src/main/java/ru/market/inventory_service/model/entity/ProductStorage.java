package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProductStorage {

    @Id
    @GeneratedValue
    private Integer productStorageId;

    @ManyToOne
    @JoinColumn(name = "shelf_id", nullable = false)
    private Shelf shelf;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
