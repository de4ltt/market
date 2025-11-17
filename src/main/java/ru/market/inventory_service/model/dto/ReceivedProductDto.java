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

    private Integer receivedProductId = null;

    @NonNull
    private Integer employeeId;

    @NonNull
    private ProductDto product;

    @NonNull
    private LocalDate date;

    @NonNull
    private String status;

    @NonNull
    private Integer quantity = 1;

    @NonNull
    private LocalDate expirationDate;

    private String comment;
}