package ru.market.inventory_service.repository;

import org.jetbrains.annotations.NotNull;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.inventory_service.model.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @NotNull
    @Named("productById")
    Product getReferenceById(@NotNull Integer id);
    List<Product> findAllByName(String name);
}
