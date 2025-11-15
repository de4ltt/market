package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceivedProductDto {

    @NonNull
    private Integer receivedProductId;

    @NonNull
    private Integer employeeId;

    @NonNull
    private Integer productId;

    @NonNull
    private LocalDate date;

    @NonNull
    private String status;

    private String comment;
}