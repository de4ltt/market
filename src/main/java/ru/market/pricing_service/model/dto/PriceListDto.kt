package ru.market.pricing_service.model.dto

import java.time.LocalDate

data class PriceListDto(
    val priceListId: Int,
    val type: String,
    val effectiveDate: LocalDate,
    val endDate: LocalDate
)