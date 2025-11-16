package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStorageDto {

    private Integer productStorageId = null;

    @NonNull
    private Integer shelfId;

    @NonNull
    private Integer productId;
}
