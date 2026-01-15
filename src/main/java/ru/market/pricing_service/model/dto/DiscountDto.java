package ru.market.pricing_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {
    private Integer discountId = null;
    private String discountType;
    private BigDecimal discountSize;
}