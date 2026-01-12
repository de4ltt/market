package ru.market.pricing_service.model.dto

import ru.market.inventory_service.model.dto.ProductDto

data class PriceOrderResponse(
    val updatedProducts: List<ProductDto>,
    val stopList: List<StopListItemDto>,
    val printPriceTags: List<PriceTag>,
    val printCoupons: List<CouponDto>
)