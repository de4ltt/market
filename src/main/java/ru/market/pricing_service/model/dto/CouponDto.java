package ru.market.pricing_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {
    private Integer batchId;
    private LocalDate expiryDate;
    private BigDecimal discountSize;
    private String comment;
}
