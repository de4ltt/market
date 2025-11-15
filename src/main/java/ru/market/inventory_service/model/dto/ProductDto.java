package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @NonNull
    private Integer productId;

    @NonNull
    private String name;

    @NonNull
    private String manufacturerName;

    @NonNull
    private String manufacturerCountry;

    @NonNull
    private String manufacturerCode;

    @NonNull
    private String size;

    @NonNull
    private String unit;

    @NonNull
    private LocalDate expirationDate;

    @NonNull
    private String barcode;

    private String additionalInfo;
}
