package ru.market.pricing_service.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import ru.market.hr_service.model.entity.Employee
import java.time.LocalDate

@Entity
data class Check(
    @Id @GeneratedValue val checkId: Int,
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    val employee: Employee,
    @Column val date: LocalDate
)