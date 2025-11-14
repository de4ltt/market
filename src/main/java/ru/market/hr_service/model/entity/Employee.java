package ru.market.hr_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue
    private Integer employeeId;

    @Column
    private String fullName;

    @Column(length = 10)
    private String passportSeries;

    @Column(length = 20)
    private String passportNumber;

    @Column
    private String registrationAddress;

    @Column
    private LocalDate birthDate;

    @OneToOne
    private Position position;

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String login;

    @Column
    private String password;

    @Column(length = 50)
    private String role;

    @Column(length = 20)
    private String workPhone;

    @Column(length = 20)
    private String personalPhone;

    @Column(length = 100)
    private String email;

}
