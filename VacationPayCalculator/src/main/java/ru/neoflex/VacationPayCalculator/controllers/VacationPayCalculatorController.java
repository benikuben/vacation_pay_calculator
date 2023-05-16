package ru.neoflex.VacationPayCalculator.controllers;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.neoflex.VacationPayCalculator.dto.VacationPayDTO;
import ru.neoflex.VacationPayCalculator.services.VacationPayCalculatorService;
import ru.neoflex.VacationPayCalculator.util.ErrorResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@Validated
public class VacationPayCalculatorController {
    private final VacationPayCalculatorService vacationPayCalculatorService;

    @Autowired
    public VacationPayCalculatorController(VacationPayCalculatorService vacationPayCalculatorService) {
        this.vacationPayCalculatorService = vacationPayCalculatorService;
    }

    @GetMapping("/calculate")
    public VacationPayDTO calculate(@RequestParam("average_salary")
                                    @Positive(message = "Введено отрицательное значение")
                                    @Digits(integer = 10, fraction = 2, message = "Значение должно быть в формате 0.00") BigDecimal averageSalary,
                                    @RequestParam("vacation_days")
                                    @Positive(message = "Введено отрицательное значение") Integer vacationDays,
                                    @RequestParam("start_of_vacation")
                                    @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<LocalDate> startOfVacation) {
        if (startOfVacation.isPresent())
            vacationDays = vacationPayCalculatorService.getVacationDaysFromStartDate(startOfVacation.get(), vacationDays);
        return vacationPayCalculatorService.calculateVacationPay(averageSalary, vacationDays);
    }

    //исключение будет выброшено, если не будет указана средняя зарплата/количество дней отпуска
    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ResponseEntity<ErrorResponse> handleException(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(new ErrorResponse("Недостаточно данных для расчета"), HttpStatus.BAD_REQUEST);
    }

    //исключение будет выброшено, если не удастся сконвертировать значения, пришедшие в параметрах запроса
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(new ErrorResponse("Некорректно введены данные"), HttpStatus.BAD_REQUEST);
    }

    //исключение будет выброшено, если значения не пройдут валидацию
    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<ErrorResponse> handleException(ConstraintViolationException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
