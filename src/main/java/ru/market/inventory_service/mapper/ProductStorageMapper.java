package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.inventory_service.model.dto.ProductStorageDto;
import ru.market.inventory_service.model.entity.ProductStorage;
import ru.market.inventory_service.repository.ProductRepository;
import ru.market.inventory_service.repository.ShelfRepository;

@Mapper(componentModel = "spring",
        uses = {ShelfRepository.class, ProductRepository.class})
public interface ProductStorageMapper extends EntityMapper<ProductStorage, ProductStorageDto> {

    @Mapping(target = "shelfId",   source = "shelf.shelfId")
    @Mapping(target = "productId", source = "product.productId")
    ProductStorageDto toDto(ProductStorage productStorage);

    @Mapping(target = "shelf",   source = "shelfId",   qualifiedByName = "shelfById")
    @Mapping(target = "product", source = "productId", qualifiedByName = "productById")
    ProductStorage toEntity(ProductStorageDto dto);
}