package ru.market.inventory_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class StorageLocation {

    @Id
    @GeneratedValue
    private Integer storageLocationId;

    @Column
    private String name;

    @Column(length = 50)
    private String type;

    @Column
    private String address;

}
