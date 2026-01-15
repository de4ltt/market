package ru.market.pricing_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopListItemDto {
    private Integer productId;
    private String reason;
    private LocalDate date;
}
