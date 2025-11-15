package ru.market.inventory_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contact_person")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactPerson {

    @Id
    @GeneratedValue
    private Integer contactPersonId;

    @Column(nullable = false)
    private String fullName;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(length = 100, nullable = false)
    private String email;
}
