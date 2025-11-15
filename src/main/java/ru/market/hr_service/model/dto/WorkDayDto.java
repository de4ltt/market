package ru.market.hr_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDayDto {

    @NonNull
    private Integer workDayId;

    @NonNull
    private Integer employeeId;

    @NonNull
    private LocalDate date;

    @NonNull
    private LocalDateTime checkIn;

    @NonNull
    private LocalDateTime checkOut;

    @NonNull
    private BigDecimal hoursWorked = BigDecimal.ZERO;

    @NonNull
    private BigDecimal overtime = BigDecimal.ZERO;

    @NonNull
    private BigDecimal underwork = BigDecimal.ZERO;
}