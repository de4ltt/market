package ru.market.inventory_service.model.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import ru.market.hr_service.model.entity.Employee;

import java.time.LocalDate;

@Data
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