package ru.market.pricing_service.model.dto

import java.time.LocalDate

data class CheckDto(
    val checkId: Int,
    val employeeId: Int,
    val date: LocalDate
)