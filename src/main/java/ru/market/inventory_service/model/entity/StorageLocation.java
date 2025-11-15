package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "storage_location")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
