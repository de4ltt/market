package ru.market.inventory_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
