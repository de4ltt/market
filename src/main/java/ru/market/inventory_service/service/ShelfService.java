package ru.market.inventory_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.market.inventory_service.core.service.CRUDService;
import ru.market.inventory_service.mapper.ShelfMapper;
import ru.market.inventory_service.model.dto.ShelfDto;
import ru.market.inventory_service.model.entity.Shelf;
import ru.market.inventory_service.repository.ShelfRepository;

@Service
public class ShelfService extends CRUDService<Shelf, ShelfDto> {
    @Autowired
    public ShelfService(ShelfRepository shelfRepository, ShelfMapper shelfMapper) {
        super(shelfRepository, shelfMapper);
    }
}
