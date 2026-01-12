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

    private String barcode;

    private String additionalInfo;

    private String storageRequirement;

    public Integer getProductId() {
        return productId;
    }

    public @NonNull String getName() {
        return name;
    }

    public @NonNull String getManufacturerName() {
        return manufacturerName;
    }

    public @NonNull String getManufacturerCountry() {
        return manufacturerCountry;
    }

    public @NonNull String getManufacturerCode() {
        return manufacturerCode;
    }

    public @NonNull String getSize() {
        return size;
    }

    public @NonNull String getUnit() {
        return unit;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public String getStorageRequirement() {
        return storageRequirement;
    }
}