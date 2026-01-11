package ru.market.hr_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue
    private Integer employeeId;

    @Column(nullable = false)
    private String fullName;

    @Column(length = 10, nullable = false)
    private String passportSeries;

    @Column(length = 20, nullable = false)
    private String passportNumber;

    @Column(nullable = false)
    private String registrationAddress;

    @Column(nullable = false)
    private LocalDate birthDate;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @Column(length = 100, nullable = false)
    private String department;

    @Column(length = 100, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String role;

    @Column(length = 20, nullable = false)
    private String workPhone;

    @Column(length = 20, nullable = false)
    private String personalPhone;

    @Column(length = 100, nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private WorkScheduleTemplate template;

}
