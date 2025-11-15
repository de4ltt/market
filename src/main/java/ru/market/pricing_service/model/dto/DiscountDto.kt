package ru.market.pricing_service.model.dto

import java.math.BigDecimal

data class DiscountDto(
    val discountId: Int,
    val discountType: String,
    val discountSize: BigDecimal
)