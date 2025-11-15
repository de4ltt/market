package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockOperationDto {

    @NonNull
    private Integer stockOperationId;

    @NonNull
    private Integer productId;

    @NonNull
    private Integer responsibleEmployeeId;

    @NonNull
    private Integer storageLocationId;

    @NonNull
    private Integer shelfId;

    @NonNull
    private Integer quantity = 0;

    @NonNull
    private String operationType;

    @NonNull
    private LocalDate expiryDate;

    @NonNull
    private String reason;

    @NonNull
    private LocalDate timestamp;

}