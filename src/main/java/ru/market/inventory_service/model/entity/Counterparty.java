package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "counterparty")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Counterparty {

    @Id
    @GeneratedValue
    private Integer counterpartyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String contactInfo;
}
