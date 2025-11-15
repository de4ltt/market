package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shelf")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shelf {

    @Id
    @GeneratedValue
    private Integer shelfId;

    @Column(length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "storage_location_id", nullable = false)
    private StorageLocation storageLocation;

    @Column(length = 50, nullable = false)
    private String type;
}
