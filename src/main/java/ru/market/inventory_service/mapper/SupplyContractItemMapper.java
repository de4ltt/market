package ru.market.inventory_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.inventory_service.model.dto.SupplyContractItemDto;
import ru.market.inventory_service.model.entity.SupplyContractItem;
import ru.market.inventory_service.repository.ProductRepository;
import ru.market.inventory_service.repository.SupplyContractRepository;

@Mapper(
        componentModel = "spring",
        uses = {SupplyContractRepository.class, ProductRepository.class}
)
public interface SupplyContractItemMapper extends EntityMapper<SupplyContractItem, SupplyContractItemDto> {

    @Mapping(target = "supplyContractId", source = "supplyContract.supplyContractId")
    @Mapping(target = "productId", source = "product.productId")
    SupplyContractItemDto toDto(SupplyContractItem entity);

    @Mapping(target = "supplyContract", source = "supplyContractId", qualifiedByName = "supplyContractById")
    @Mapping(target = "product", source = "productId", qualifiedByName = "productById")
    SupplyContractItem toEntity(SupplyContractItemDto dto);
}
