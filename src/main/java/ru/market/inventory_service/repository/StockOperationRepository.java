package ru.market.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.inventory_service.model.entity.StockOperation;

@Repository
public interface StockOperationRepository extends JpaRepository<StockOperation, Integer> {
}
