package ru.market.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.inventory_service.model.entity.StockOperation;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockOperationRepository extends JpaRepository<StockOperation, Integer> {
    List<StockOperation> findByExpiryDateBefore(LocalDate date);
}
