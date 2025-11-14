package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.market.inventory_service.model.dto.ProductDto;
import ru.market.inventory_service.model.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product entity);

    Product toEntity(ProductDto dto);
}
