package ru.market.pricing_service.mapper

import org.mapstruct.Mapper
import ru.market.pricing_service.model.dto.PriceListDto
import ru.market.pricing_service.model.entity.PriceList

@Mapper(componentModel = "spring")
interface PriceListMapper {
    fun toDto(entity: PriceList): PriceListDto
    fun toEntity(dto: PriceListDto): PriceList
}