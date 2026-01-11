package ru.market.pricing_service.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.market.inventory_service.core.service.MarketInventoryCRUDService
import ru.market.pricing_service.mapper.ProductPriceInCheckMapper
import ru.market.pricing_service.model.dto.ProductPriceInCheckDto
import ru.market.pricing_service.model.entity.ProductPriceInCheck
import ru.market.pricing_service.repository.ProductPriceInCheckRepository
import java.math.BigDecimal
import java.time.LocalDate

//@Service
//class ProductPriceInCheckService @Autowired constructor(
//    repository: ProductPriceInCheckRepository,
//    mapper: ProductPriceInCheckMapper
//) : MarketInventoryCRUDService<ProductPriceInCheck, ProductPriceInCheckDto>(repository, mapper) {
//
//    // Additional methods if needed for pricing, e.g., historical prices for change checks
//    fun getLastPriceForProduct(productId: Int): BigDecimal? {
//        val last = repository.findTopByProductIdOrderByCheckDateDesc(productId)
//        return last?.finalPrice
//    }
//
//    fun getSalesDataForProduct(productId: Int, from: LocalDate, to: LocalDate): SalesData {
//        val prices = repository.findByProductIdAndCheckDateBetween(productId, from, to)
//        val quantitySold = prices.sumOf { it.quantity ?: 0 }
//        val revenue = prices.fold(BigDecimal.ZERO) { acc, ppc -> acc.add(ppc.finalPrice.multiply(BigDecimal(ppc.quantity ?: 0))) }
//        return SalesData(quantitySold, revenue)
//    }
//}