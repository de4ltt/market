package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private Integer productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String manufacturerName;

    @Column(length = 100, nullable = false)
    private String manufacturerCountry;

    @Column(length = 50, nullable = false)
    private String manufacturerCode;

    @Column(length = 50, nullable = false)
    private String size;

    @Column(length = 50, nullable = false)
    private String unit;

    @Column(length = 50, nullable = false)
    private String barcode;

    @Column
    private String additionalInfo;

    @Column(length = 50)
    private String storageRequirement;
}
