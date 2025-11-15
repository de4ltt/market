package ru.market.pricing_service.repository

import org.mapstruct.Named
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.market.pricing_service.model.entity.PriceList

@Repository
interface PriceListRepository : JpaRepository<PriceList, Int> {
    @Named("priceListById")
    override fun getReferenceById(id: Int): PriceList
}