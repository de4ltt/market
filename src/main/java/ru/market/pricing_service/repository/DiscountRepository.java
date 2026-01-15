package ru.market.pricing_service.repository;

import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.pricing_service.model.entity.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {}