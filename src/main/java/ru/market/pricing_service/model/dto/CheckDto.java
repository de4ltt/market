package ru.market.pricing_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckDto {
    private Integer checkId = null;
    private Integer employeeId;
    private LocalDate date;
}