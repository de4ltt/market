package ru.market.pricing_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceListDto {
    private Integer priceListId = null;
    private String type;
    private LocalDate effectiveDate;
    private LocalDate endDate;
}