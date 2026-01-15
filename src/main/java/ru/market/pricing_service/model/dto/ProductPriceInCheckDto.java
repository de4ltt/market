package ru.market.pricing_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceInCheckDto {
    private Integer productPriceInCheckId = null;
    private Integer productId;
    private Integer checkId;
    private Integer priceListId;
    private Integer discountId;
    private BigDecimal inputPrice;
    private BigDecimal finalPrice;
    private String priceType;
}