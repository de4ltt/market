package ru.market.pricing_service.mapper

import org.mapstruct.Mapper
import ru.market.pricing_service.model.dto.DiscountDto
import ru.market.pricing_service.model.entity.Discount

@Mapper(componentModel = "spring")
interface DiscountMapper {
    fun toDto(entity: Discount): DiscountDto
    fun toEntity(dto: DiscountDto): Discount
}