package ru.market.pricing_service.model.entity

import jakarta.persistence.*
import ru.market.inventory_service.model.entity.Product
import java.math.BigDecimal

@Entity
data class ProductPriceInCheck(
    @Id
    @GeneratedValue
    val productPriceInCheckId: Int,

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product,

    @ManyToOne
    @JoinColumn(name = "check_id", nullable = false)
    val check: Check,

    @ManyToOne
    @JoinColumn(name = "price_list_id", nullable = false)
    val priceList: PriceList,

    @OneToOne
    @JoinColumn(name = "discount_id", nullable = false)
    val discount: Discount,

    @Column val storeId: Int,
    @Column(precision = 10, scale = 2) val inputPrice: BigDecimal,
    @Column(precision = 10, scale = 2) val finalPrice: BigDecimal,
    @Column(precision = 10, scale = 2) val priceType: BigDecimal
)
