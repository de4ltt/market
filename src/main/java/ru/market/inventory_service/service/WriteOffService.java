package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.CRUDService;
import ru.market.inventory_service.mapper.WriteOffMapper;
import ru.market.inventory_service.model.dto.WriteOffDto;
import ru.market.inventory_service.model.entity.WriteOff;
import ru.market.inventory_service.repository.WriteOffRepository;

@Service
public class WriteOffService extends CRUDService<WriteOff, WriteOffDto> {
    @Autowired
    public WriteOffService(WriteOffRepository writeOffRepository, WriteOffMapper writeOffMapper) {
        super(writeOffRepository, writeOffMapper);
    }
}
