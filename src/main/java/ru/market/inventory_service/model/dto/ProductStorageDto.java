package ru.market.inventory_service.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ProductStorageDto {

    @NonNull
    private Integer productStorageId;

    @NonNull
    private Integer shelfId;

    @NonNull
    private Integer productId;

}
