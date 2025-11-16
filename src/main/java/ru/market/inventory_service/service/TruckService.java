package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.CRUDService;
import ru.market.inventory_service.mapper.TruckMapper;
import ru.market.inventory_service.model.dto.TruckDto;
import ru.market.inventory_service.model.entity.Truck;
import ru.market.inventory_service.repository.TruckRepository;

@Service
public class TruckService extends CRUDService<Truck, TruckDto> {
    @Autowired
    public TruckService(TruckRepository truckRepository, TruckMapper truckMapper) {
        super(truckRepository, truckMapper);
    }
}
