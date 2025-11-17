package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Integer productId = null;

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
    private String barcode;

    private String additionalInfo;
}
