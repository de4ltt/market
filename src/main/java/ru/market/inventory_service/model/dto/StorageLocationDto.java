package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageLocationDto {

    private Integer storageLocationId = null;

    @NonNull
    private String name;

    @NonNull
    private String type;

    @NonNull
    private String address;
}
