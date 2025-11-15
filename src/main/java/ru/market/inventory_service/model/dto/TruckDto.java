package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckDto {

    @NonNull
    private Integer truckId;

    @NonNull
    private String licencePlate;

    @NonNull
    private String model;

    @NonNull
    private BigDecimal capacity;

    @NonNull
    private Integer driverId;
}
