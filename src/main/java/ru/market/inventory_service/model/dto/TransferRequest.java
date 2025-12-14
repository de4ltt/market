package ru.market.inventory_service.model.dto;

import lombok.Data;

@Data
public class TransferRequest {
    private Integer fromLocationId;
    private Integer toLocationId;
    private Integer productId;
    private Integer quantity;
}