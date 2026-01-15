package ru.market.pricing_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.market.inventory_service.model.dto.ProductDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceOrderResponse {
    private List<ProductDto> updatedProducts;
    private List<StopListItemDto> stopList;
    private List<PriceTag> printPriceTags;
    private List<CouponDto> printCoupons;
}
