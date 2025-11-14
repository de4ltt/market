package ru.market.hr_service.model.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@Data
public class Position {
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
