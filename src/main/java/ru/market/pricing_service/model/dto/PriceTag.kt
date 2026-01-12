package ru.market.pricing_service.model.dto

import java.math.BigDecimal

data class PriceTag(
    val productName: String,
    val price: BigDecimal,
    val tagType: String, // белый, желтый, акционный
    val regularPrice: BigDecimal,
    val promoDescription: String? = null // Добавлено для поддержки описания специальных акций типа "1+1", "2+1" и т.д.
)