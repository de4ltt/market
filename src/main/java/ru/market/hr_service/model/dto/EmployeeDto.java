package ru.market.hr_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    private Integer employeeId = null;

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

    @NonNull
    public String getRole() {
        return role;
    }
}
