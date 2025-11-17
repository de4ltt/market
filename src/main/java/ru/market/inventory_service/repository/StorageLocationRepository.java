package ru.market.inventory_service.repository;

import org.jetbrains.annotations.NotNull;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.inventory_service.model.entity.StorageLocation;

import java.util.Optional;

@Repository
public interface StorageLocationRepository extends JpaRepository<StorageLocation, Integer> {
    @NotNull
    @Named("storageLocationById")
    StorageLocation getReferenceById(@NotNull Integer id);
    Optional<StorageLocation> findFirstByType(String type);
}
