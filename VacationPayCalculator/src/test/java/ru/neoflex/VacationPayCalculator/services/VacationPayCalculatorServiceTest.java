package ru.neoflex.VacationPayCalculator.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.neoflex.VacationPayCalculator.dto.VacationPayDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class VacationPayCalculatorServiceTest {
    private static VacationPayCalculatorService vacationPayCalculatorService;
    private final BigDecimal testAverageSalary = BigDecimal.valueOf(100000);

    @BeforeAll
    public static void createNewVacationPayCalculatorService() {
        vacationPayCalculatorService = new VacationPayCalculatorService();
    }

    @Test
    void calculateVacationPayWithDaysTest() {
        Integer testVacationDays = 28;
        VacationPayDTO actual = vacationPayCalculatorService.calculateVacationPayWithDays(testAverageSalary, testVacationDays);
        assertEquals(new VacationPayDTO("Отпускные: ", BigDecimal.valueOf(95563.16)), actual);
    }

    @Test
    void calculateVacationPayWithDatesTest() {
        LocalDate testStartOfVacation = LocalDate.of(2023, Month.MAY, 1);
        LocalDate testEndOfVacation = LocalDate.of(2023, Month.JUNE, 13);
        VacationPayDTO actual = vacationPayCalculatorService.calculateVacationPayWithDates(testAverageSalary, testStartOfVacation, testEndOfVacation);
        assertEquals(new VacationPayDTO("Отпускные: ", BigDecimal.valueOf(95563.16)), actual);
    }
}