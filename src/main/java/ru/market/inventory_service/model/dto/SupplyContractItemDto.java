package ru.market.inventory_service.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
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
