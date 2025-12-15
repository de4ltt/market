package ru.market.inventory_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.market.hr_service.repository.EmployeeRepository;
import ru.market.inventory_service.core.service.MarketInventoryCRUDService;
import ru.market.inventory_service.mapper.InventoryMapper;
import ru.market.inventory_service.model.dto.InventoryDto;
import ru.market.inventory_service.model.entity.Inventory;
import ru.market.inventory_service.repository.InventoryRepository;
import ru.market.inventory_service.repository.ProductRepository;

import java.time.LocalDate;

@Service
public class InventoryService extends MarketInventoryCRUDService<Inventory, InventoryDto> {

    private final ProductRepository productRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper, ProductRepository productRepository, EmployeeRepository employeeRepository) {
        super(inventoryRepository, inventoryMapper);
        this.productRepository = productRepository;
        this.employeeRepository = employeeRepository;
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void performMonthlyInventory() {
        var employee = employeeRepository.findById(1).orElseThrow();
        var products = productRepository.findAll();
        products.forEach(product -> {
            Inventory inventory = new Inventory();
            inventory.setDate(LocalDate.now());
            inventory.setEmployee(employee);
            inventory.setProduct(product);
            add(super.getEntityMapper().toDto(inventory));
        });
    }
}