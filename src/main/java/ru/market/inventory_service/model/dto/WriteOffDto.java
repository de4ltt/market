package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
