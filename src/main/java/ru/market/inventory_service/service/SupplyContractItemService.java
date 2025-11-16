package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.CRUDService;
import ru.market.inventory_service.mapper.SupplyContractItemMapper;
import ru.market.inventory_service.model.dto.SupplyContractItemDto;
import ru.market.inventory_service.model.entity.SupplyContractItem;
import ru.market.inventory_service.repository.SupplyContractItemRepository;

@Service
public class SupplyContractItemService extends CRUDService<SupplyContractItem, SupplyContractItemDto> {
    @Autowired
    public SupplyContractItemService(SupplyContractItemRepository supplyContractItemRepository, SupplyContractItemMapper supplyContractItemMapper) {
        super(supplyContractItemRepository, supplyContractItemMapper);
    }
}
