package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.MarketInventoryCRUDService;
import ru.market.inventory_service.mapper.WriteOffMapper;
import ru.market.inventory_service.model.dto.WriteOffDto;
import ru.market.inventory_service.model.entity.WriteOff;
import ru.market.inventory_service.repository.StockOperationRepository;
import ru.market.inventory_service.repository.WriteOffRepository;

import java.time.LocalDate;

@Service
public class WriteOffService extends MarketInventoryCRUDService<WriteOff, WriteOffDto> {

    private final StockOperationRepository stockOperationRepository;

    @Autowired
    public WriteOffService(WriteOffRepository writeOffRepository, WriteOffMapper writeOffMapper, StockOperationRepository stockOperationRepository) {
        super(writeOffRepository, writeOffMapper);
        this.stockOperationRepository = stockOperationRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void autoWriteOffExpiredProducts() {
        var expiredOps = stockOperationRepository.findByExpiryDateBefore(LocalDate.now());
        expiredOps.forEach(op -> {
            WriteOffDto dto = new WriteOffDto();
            dto.setEmployeeId(op.getResponsibleEmployee().getEmployeeId());
            dto.setProductId(op.getProduct().getProductId());
            dto.setDate(LocalDate.now());
            dto.setWriteOffReason("Expired");
            dto.setComment("Auto write-off due to expiry date: " + op.getExpiryDate());
            add(dto);
        });
    }
}
