package ru.market.inventory_service.model.dto;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class InventoryDto {

    @NonNull
    private Integer inventoryId;

    @NonNull
    private LocalDate date;

    @NonNull
    private Integer employeeId;

    @NonNull
    private Integer productId;

}
