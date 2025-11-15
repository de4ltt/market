package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyContractItemDto {

    @NonNull
    private Integer supplyContractItemId;

    @NonNull
    private Integer supplyContractId;

    @NonNull
    private Integer productId;

    @NonNull
    private Integer quantity;

    @NonNull
    private String deliveryType;

}
