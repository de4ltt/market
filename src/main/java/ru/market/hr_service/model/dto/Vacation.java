package ru.market.hr_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vacation {
    @NonNull
    private Integer vacationId;
    @NonNull
    private Integer employeeId;
    @NonNull
    private String type;
    @NonNull
    private LocalDate startDate;
    @NonNull
    private LocalDate endDate;
    @NonNull
    private Boolean approved = false;
}
