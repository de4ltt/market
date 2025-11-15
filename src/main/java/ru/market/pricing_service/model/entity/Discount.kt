package ru.market.pricing_service.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
data class Discount(
    @Id
    @GeneratedValue
    val discountId: Int,

    @Column(length = 100) val discountType: String,
    @Column(precision = 10, scale = 2) val discountSize: BigDecimal
)