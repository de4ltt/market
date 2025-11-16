package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyContractDto {

    private Integer supplyContractId = null;

    @NonNull
    private Integer contractorId;

    @NonNull
    private Integer storageLocationId;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private LocalDate endDate;
}
