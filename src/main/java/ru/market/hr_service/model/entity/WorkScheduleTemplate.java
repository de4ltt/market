package ru.market.hr_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "work_schedule_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkScheduleTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;          // "Офис Пн-Пт", "Сменный Вт-Сб", "Выходные + Пн"

    @Column(length = 255)
    private String description;

    // Рабочие дни недели (можно выбрать любые 5 из 7)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "schedule_working_days",
        joinColumns = @JoinColumn(name = "template_id")
    )
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> workingDays = new HashSet<>();  // MONDAY, TUESDAY, ..., SUNDAY

    @Column(nullable = false)
    private BigDecimal hoursPerDay = new BigDecimal("8.00");

    @Column(nullable = false)
    private BigDecimal weeklyHours = new BigDecimal("40.00");

    // Для валидации: ровно 5 рабочих дней
    @PrePersist
    @PreUpdate
    private void validate() {
        if (workingDays == null || workingDays.size() != 5) {
            throw new IllegalStateException("График должен содержать ровно 5 рабочих дней");
        }
        if (!weeklyHours.equals(hoursPerDay.multiply(BigDecimal.valueOf(5)))) {
            throw new IllegalStateException("Норма недели должна быть 40 часов (8 × 5)");
        }
    }
}