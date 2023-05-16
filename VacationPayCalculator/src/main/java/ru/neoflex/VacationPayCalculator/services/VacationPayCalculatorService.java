package ru.neoflex.VacationPayCalculator.services;

import org.springframework.stereotype.Service;
import ru.neoflex.VacationPayCalculator.dto.VacationPayDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VacationPayCalculatorService {
    private static final float AVERAGE_NUMBER_OF_DAYS_IN_MONTH = 29.3F;
    private static final int YEAR = 2023;
    private static final List<LocalDate> HOLIDAYS = List.of(
            LocalDate.of(YEAR, Month.JANUARY, 2),
            LocalDate.of(YEAR, Month.JANUARY, 3),
            LocalDate.of(YEAR, Month.JANUARY, 4),
            LocalDate.of(YEAR, Month.JANUARY, 5),
            LocalDate.of(YEAR, Month.JANUARY, 6),
            LocalDate.of(YEAR, Month.FEBRUARY, 23),
            LocalDate.of(YEAR, Month.FEBRUARY, 24),
            LocalDate.of(YEAR, Month.MARCH, 8),
            LocalDate.of(YEAR, Month.MAY, 1),
            LocalDate.of(YEAR, Month.MAY, 8),
            LocalDate.of(YEAR, Month.MAY, 9),
            LocalDate.of(YEAR, Month.JUNE, 12),
            LocalDate.of(YEAR, Month.NOVEMBER, 6));

    private Integer getVacationDaysFromDates(LocalDate startOfVacation, LocalDate endOfVacation) {
        //количество дней между датой начала и датой окончания отпуска без учета субботы и воскресенья
        int vacationDaysWithoutWeekends = (int) startOfVacation.datesUntil(endOfVacation.plusDays(1))
                .collect(Collectors.toList()).stream().
                filter(date->!isWeekend(date)).count();

        //количество праздничных дней между датой начала и датой окончания отпуска с учетом того,
        // что праздничный день не выпадает на субботу или воскресенье
        int holidaysDuringVacation= (int) HOLIDAYS.stream().
                filter(holiday->!isWeekend(holiday)&&
                        !holiday.isAfter(endOfVacation) &&
                        !holiday.isBefore(startOfVacation)).count();

        return vacationDaysWithoutWeekends-holidaysDuringVacation;
    }

    //проверка, выпадает ли дата на субботу или воскресенье
    private Boolean isWeekend(LocalDate day) {
        return (day.getDayOfWeek().equals(DayOfWeek.SATURDAY) || day.getDayOfWeek().equals(DayOfWeek.SUNDAY));
    }

    public VacationPayDTO calculateVacationPayWithDays(BigDecimal averageSalary, Integer vacationDays) {
        BigDecimal vacationPay= averageSalary.
                divide(BigDecimal.valueOf(AVERAGE_NUMBER_OF_DAYS_IN_MONTH), 2, RoundingMode.HALF_EVEN).
                multiply(BigDecimal.valueOf(vacationDays));
        return new VacationPayDTO("Отпускные: ", vacationPay);
    }
    public VacationPayDTO calculateVacationPayWithDates(BigDecimal averageSalary, LocalDate startOfVacation,LocalDate endOfVacation) {
        Integer vacationDays=getVacationDaysFromDates(startOfVacation,endOfVacation);
        return calculateVacationPayWithDays(averageSalary, vacationDays);
    }
}

