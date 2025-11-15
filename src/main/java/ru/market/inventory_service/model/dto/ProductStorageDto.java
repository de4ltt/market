package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStorageDto {

    @NonNull
    private Integer productStorageId;

    @NonNull
    private Integer shelfId;

    @NonNull
    private Integer productId;
}
