package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import ru.market.inventory_service.model.dto.ProductDto;
import ru.market.inventory_service.model.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product entity);
    Product toEntity(ProductDto dto);
}
