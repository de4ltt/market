package ru.market.pricing_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.market.inventory_service.model.entity.Product;
import ru.market.pricing_service.model.entity.Check;
import ru.market.pricing_service.model.entity.ProductPriceInCheck;
import java.util.List;

@Repository
public interface ProductPriceInCheckRepository extends JpaRepository<ProductPriceInCheck, Integer> {

    @Query("SELECT ppic FROM ProductPriceInCheck ppic " +
            "JOIN FETCH ppic.priceList " +
            "JOIN FETCH ppic.product " +
            "WHERE ppic.check = :check")
    List<ProductPriceInCheck> findByCheck(@Param("check") Check check);

    @Query("SELECT ppic FROM ProductPriceInCheck ppic " +
            "JOIN FETCH ppic.priceList " +
            "WHERE ppic.check = :check AND ppic.product = :product")
    List<ProductPriceInCheck> findByCheckAndProduct(
            @Param("check") Check check,
            @Param("product") Product product);
}