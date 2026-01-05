package ru.market.pricing_service.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.market.inventory_service.model.entity.Product
import ru.market.pricing_service.model.entity.Check
import ru.market.pricing_service.model.entity.ProductPriceInCheck

@Repository
interface ProductPriceInCheckRepository : JpaRepository<ProductPriceInCheck, Int> {

    @Query("""
        SELECT ppic FROM ProductPriceInCheck ppic 
        JOIN FETCH ppic.priceList 
        JOIN FETCH ppic.product 
        WHERE ppic.check = :check
    """)
    fun findByCheck(@Param("check") check: Check): List<ProductPriceInCheck>

    @Query("""
        SELECT ppic FROM ProductPriceInCheck ppic 
        JOIN FETCH ppic.priceList 
        WHERE ppic.check = :check AND ppic.product = :product
    """)
    fun findByCheckAndProduct(
        @Param("check") check: Check,
        @Param("product") product: Product
    ): List<ProductPriceInCheck>
}