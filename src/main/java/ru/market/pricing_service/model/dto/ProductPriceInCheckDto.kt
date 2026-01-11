package ru.market.pricing_service.model.dto

import java.math.BigDecimal

data class ProductPriceInCheckDto(
    val productPriceInCheckId: Int? = null,
    val productId: Int,
    val checkId: Int,
    val priceListId: Int,
    val discountId: Int,
    val inputPrice: BigDecimal,
    val finalPrice: BigDecimal,
    val priceType: String
)
