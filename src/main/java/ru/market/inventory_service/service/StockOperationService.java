package ru.market.inventory_service.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.MarketInventoryCRUDService;
import ru.market.inventory_service.mapper.StockOperationMapper;
import ru.market.inventory_service.model.dto.StockOperationDto;
import ru.market.inventory_service.model.entity.StockOperation;
import ru.market.inventory_service.repository.StockOperationRepository;

@Service
public class StockOperationService extends MarketInventoryCRUDService<StockOperation, StockOperationDto> {
    @Autowired
    public StockOperationService(StockOperationRepository stockOperationRepository, StockOperationMapper stockOperationMapper) {
        super(stockOperationRepository, stockOperationMapper);
    }

    @Getter
    public enum StockOperationType {
        ADD("add"),
        REMOVE("remove");

        private final String type;

        StockOperationType(String type) {
            this.type = type;
        }
    }
}
