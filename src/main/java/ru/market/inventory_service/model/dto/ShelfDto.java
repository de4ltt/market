package ru.market.inventory_service.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ShelfDto {

    @NonNull
    private Integer shelfId;

    @NonNull
    private String name;

    @NonNull
    private Integer storageLocationId;

    @NonNull
    private String type;

}
