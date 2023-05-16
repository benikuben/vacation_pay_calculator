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
    private final Integer testVacationDays = 28;

    @BeforeAll
    public static void createNewVacationPayCalculatorService() {
        vacationPayCalculatorService = new VacationPayCalculatorService();
    }

    @Test
    void calculateVacationPayTest() {
        VacationPayDTO actual = vacationPayCalculatorService.calculateVacationPay(testAverageSalary, testVacationDays);
        assertEquals(new VacationPayDTO("Отпускные: ", BigDecimal.valueOf(95563.16)), actual);
    }

    @Test
    void getVacationDaysFromStartDateTest() {
        LocalDate testStartOfVacation = LocalDate.of(2023, Month.MAY, 1);
        Integer actual = vacationPayCalculatorService.getVacationDaysFromStartDate(testStartOfVacation, testVacationDays);
        assertEquals(17, actual);
    }
}