package ru.market.inventory_service.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ContactPersonDto {

    @NonNull
    private Integer contactPersonId;

    @NonNull
    private String fullName;

    @NonNull
    private String phone;

    @NonNull
    private String email;

}
