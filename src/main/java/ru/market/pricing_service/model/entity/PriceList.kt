package ru.market.pricing_service.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDate

@Entity
data class PriceList(
    @Id @GeneratedValue val priceListId: Int,
    @Column(length = 50) val type: String,
    @Column val effectiveDate: LocalDate,
    @Column val endDate: LocalDate
)