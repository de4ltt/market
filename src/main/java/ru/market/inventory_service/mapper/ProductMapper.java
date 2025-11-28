package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.inventory_service.model.dto.ProductDto;
import ru.market.inventory_service.model.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<Product, ProductDto> {
    @Named("productToDto")
    ProductDto toDto(Product entity);
    Product toEntity(ProductDto dto);
}
