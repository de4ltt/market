package ru.market.pricing_service.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.market.pricing_service.model.entity.ProductPriceInCheck

@Repository
interface ProductPriceInCheckRepository : JpaRepository<ProductPriceInCheck, Int>