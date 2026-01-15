package ru.market.pricing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.pricing_service.model.entity.PriceList;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceListRepository extends JpaRepository<PriceList, Integer> {
    List<PriceList> findByEffectiveDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDate date1, LocalDate date2);
}