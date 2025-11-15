package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyContractItemDto {

    private Integer supplyContractItemId = null;

    @NonNull
    private Integer supplyContractId;

    @NonNull
    private Integer productId;

    @NonNull
    private Integer quantity = 1;

    @NonNull
    private String deliveryType;
}
