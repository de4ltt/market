package ru.market.hr_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonnelReportDto {
    @NonNull
    private Integer personnelReportId;
    @NonNull
    private LocalDate date;
    @NonNull
    private Integer directorId;
    @NonNull
    private Integer employeeId;
    @NonNull
    private String status;
    @NonNull
    private BigDecimal totalHours = BigDecimal.ZERO;
    @NonNull
    private BigDecimal overtime = BigDecimal.ZERO;
    @NonNull
    private BigDecimal underwork = BigDecimal.ZERO;
    private String comment;
}
