package ru.market.pricing_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceTag {
    private String productName;
    private BigDecimal price;
    private String tagType; // белый, желтый, акционный
    private BigDecimal regularPrice;
    private String promoDescription = null; // Для поддержки описания специальных акций типа "1+1", "2+1" и т.д.
}
