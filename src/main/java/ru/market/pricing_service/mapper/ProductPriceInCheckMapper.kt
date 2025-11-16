package ru.market.pricing_service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.market.pricing_service.model.dto.ProductPriceInCheckDto
import ru.market.pricing_service.model.entity.ProductPriceInCheck

@Mapper(componentModel = "spring")
interface ProductPriceInCheckMapper {

    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "checkId", source = "check.checkId")
    @Mapping(target = "priceListId", source = "priceList.priceListId")
    @Mapping(target = "discountId", source = "discount.discountId")
    fun toDto(entity: ProductPriceInCheck): ProductPriceInCheckDto

    @Mapping(target = "product", source = "productId", qualifiedByName = ["productById"])
    @Mapping(target = "check", source = "checkId", qualifiedByName = ["checkById"])
    @Mapping(target = "priceList", source = "priceListId", qualifiedByName = ["priceListById"])
    @Mapping(target = "discount", source = "discountId", qualifiedByName = ["discountById"])
    fun toEntity(dto: ProductPriceInCheckDto): ProductPriceInCheck
}