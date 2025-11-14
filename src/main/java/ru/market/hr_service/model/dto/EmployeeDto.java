package ru.market.hr_service.model.dto;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Data
public class EmployeeDto {

    @NonNull
    private Integer employeeId;

    @NonNull
    private String fullName;

    @NonNull
    private String passportSeries;

    @NonNull
    private String passportNumber;

    @NonNull
    private String registrationAddress;

    @NonNull
    private LocalDate birthDate;

    @NonNull
    private Integer positionId;

    @NonNull
    private String department;

    @NonNull
    private String login;

    @NonNull
    private String password;

    @NonNull
    private String role;

    @NonNull
    private String workPhone;

    @NonNull
    private String personalPhone;

    @NonNull
    private String email;

}
