package ru.neoflex.VacationPayCalculator.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode //для корректного сравнения объектов при тестировании
public class VacationPayDTO {
    private String message;
    private BigDecimal vacationPay;
}
