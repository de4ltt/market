package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.MarketInventoryCRUDService;
import ru.market.inventory_service.mapper.SupplyContractMapper;
import ru.market.inventory_service.model.dto.SupplyContractDto;
import ru.market.inventory_service.model.entity.SupplyContract;
import ru.market.inventory_service.repository.SupplyContractRepository;

@Service
public class SupplyContractService extends MarketInventoryCRUDService<SupplyContract, SupplyContractDto> {
@Autowired
    public SupplyContractService(SupplyContractRepository supplyContractRepository, SupplyContractMapper supplyContractMapper) {
        super(supplyContractRepository, supplyContractMapper);
    }
}
