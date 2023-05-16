package ru.neoflex.VacationPayCalculator.services;

import org.springframework.stereotype.Service;
import ru.neoflex.VacationPayCalculator.dto.VacationPayDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class VacationPayCalculatorService {
    private static final float AVERAGE_NUMBER_OF_DAYS_IN_MONTH = 29.3F;
    private static final int YEAR = 2023;
    private static final List<LocalDate> HOLIDAYS = List.of(
            LocalDate.of(YEAR, Month.JANUARY, 1),
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

    //проверка, выпадает ли дата на субботу или воскресенье
    private Boolean isWeekend(LocalDate day) {
        return (day.getDayOfWeek().equals(DayOfWeek.SATURDAY) || day.getDayOfWeek().equals(DayOfWeek.SUNDAY));
    }

    public Integer getVacationDaysFromStartDate(LocalDate startOfVacation, Integer vacationDays) {
        //все даты с начала отпуска
        List<LocalDate> dates = new ArrayList<>(List.of(startOfVacation));
        for (int i = 1; i < vacationDays; i++)
            dates.add(dates.get(dates.size() - 1).plusDays(1));

        //количество дней с начала до окончания отпуска без учета субботы и воскресенья
        int vacationDaysWithoutWeekends = (int) dates.stream().filter(date -> !isWeekend(date)).count();

        LocalDate endOfVacation = dates.get(dates.size() - 1);
        //количество праздничных дней между датой начала и датой окончания отпуска с учетом того,
        // что праздничный день не выпадает на субботу или воскресенье
        int holidaysDuringVacation = (int) HOLIDAYS.stream().
                filter(holiday -> !isWeekend(holiday) &&
                        !holiday.isAfter(endOfVacation) &&
                        !holiday.isBefore(startOfVacation)).count();

        return vacationDaysWithoutWeekends - holidaysDuringVacation;
    }

    public VacationPayDTO calculateVacationPay(BigDecimal averageSalary, Integer vacationDays) {
        BigDecimal vacationPay = averageSalary.
                divide(BigDecimal.valueOf(AVERAGE_NUMBER_OF_DAYS_IN_MONTH), 2, RoundingMode.HALF_EVEN).
                multiply(BigDecimal.valueOf(vacationDays));
        return new VacationPayDTO("Отпускные: ", vacationPay);
    }
}

