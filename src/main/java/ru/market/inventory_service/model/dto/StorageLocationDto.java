package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
