package ru.market.inventory_service.repository;

import org.jetbrains.annotations.NotNull;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.inventory_service.model.entity.SupplyContract;

@Repository
public interface SupplyContractRepository extends JpaRepository<SupplyContract, Integer> {
    @NotNull
    @Named("supplyContractById")
    SupplyContract getReferenceById(@NotNull Integer id);
}
