package ru.market.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.inventory_service.model.entity.ReceivedProduct;

import java.util.List;

@Repository
public interface ReceivedProductRepository extends JpaRepository<ReceivedProduct, Integer> {
    List<ReceivedProduct> findByStatus(String status);
}
