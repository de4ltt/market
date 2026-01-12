package ru.market.pricing_service.model.dto

import java.time.LocalDate

data class StopListItemDto(
    val productId: Int,
    val reason: String,
    val date: LocalDate
)