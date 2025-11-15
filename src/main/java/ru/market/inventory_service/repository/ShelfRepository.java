package ru.market.inventory_service.repository;

import org.jetbrains.annotations.NotNull;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.inventory_service.model.entity.Shelf;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Integer> {
    @NotNull
    @Named("shelfById")
    Shelf getReferenceById(@NotNull Integer id);
}
