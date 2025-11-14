package ru.market.inventory_service.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class StorageLocationDto {

    @NonNull
    private Integer storageLocationId;

    @NonNull
    private String name;

    @NonNull
    private String type;

    @NonNull
    private String address;

}
