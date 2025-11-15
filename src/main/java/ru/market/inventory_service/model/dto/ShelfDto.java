package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
