package ru.market.pricing_service.model.dto

import java.math.BigDecimal
import java.time.LocalDate

data class CouponDto(
    val batchId: Int,
    val expiryDate: LocalDate,
    val discountSize: BigDecimal,
    val comment: String
)