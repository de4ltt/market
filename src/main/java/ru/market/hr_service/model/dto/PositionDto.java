package ru.market.hr_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionDto {

    @NonNull
    private Integer positionId;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private Integer monthlyHours;

    @NonNull
    private BigDecimal salaryRate;
}
