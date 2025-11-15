package ru.market.pricing_service.repository

import org.mapstruct.Named
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.market.pricing_service.model.entity.Check

@Repository
interface CheckRepository : JpaRepository<Check, Int> {
    @Named("checkById")
    override fun getReferenceById(id: Int): Check
}