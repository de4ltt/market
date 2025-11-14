package ru.market.inventory_service.model.dto;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class WriteOffDto {

    @NonNull
    private Integer writeOffId;

    @NonNull
    private Integer employeeId;

    @NonNull
    private Integer productId;

    @NonNull
    private LocalDate date;

    @NonNull
    private String writeOffReason;

    private String comment;

}
