package ru.market.pricing_service.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.market.pricing_service.model.dto.PriceOrderRequest
import ru.market.pricing_service.model.dto.PriceOrderResponse
import ru.market.pricing_service.service.PricingService

@RestController
@RequestMapping("/pricing")
open class PricingController(private val service: PricingService) {
    @PostMapping("/form-order")
    fun formOrder(@RequestBody request: PriceOrderRequest): PriceOrderResponse = service.formPrices(request.directorId)
}