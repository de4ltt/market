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

    private Integer stockOperationId = null;

    @NonNull
    private Integer productId;

    @NonNull
    private Integer responsibleEmployeeId;

    @NonNull
    private Integer storageLocationId;

    private Integer shelfId = null;

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

    public Integer getStockOperationId() {
        return stockOperationId;
    }

    public @NonNull Integer getProductId() {
        return productId;
    }

    public @NonNull Integer getResponsibleEmployeeId() {
        return responsibleEmployeeId;
    }

    public @NonNull Integer getStorageLocationId() {
        return storageLocationId;
    }

    public Integer getShelfId() {
        return shelfId;
    }

    public @NonNull Integer getQuantity() {
        return quantity;
    }

    public @NonNull String getOperationType() {
        return operationType;
    }

    public @NonNull LocalDate getExpiryDate() {
        return expiryDate;
    }

    public @NonNull String getReason() {
        return reason;
    }

    public @NonNull LocalDate getTimestamp() {
        return timestamp;
    }
}