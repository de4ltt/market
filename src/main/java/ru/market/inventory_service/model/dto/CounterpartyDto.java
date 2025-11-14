package ru.market.inventory_service.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class CounterpartyDto {

    @NonNull
    private Integer counterpartyId;

    @NonNull
    private String name;

    @NonNull
    private String address;

    @NonNull
    private String contactInfo;

}
