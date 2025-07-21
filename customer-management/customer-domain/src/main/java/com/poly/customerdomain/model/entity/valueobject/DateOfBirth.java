package com.poly.customerdomain.model.entity.valueobject;

import com.poly.customerdomain.model.exception.CustomerAgeOutOfRangeException;

import java.time.LocalDate;
import java.time.Period;

public class DateOfBirth {

    private final LocalDate value;

    private static final int MIN_AGE = 15;
    private static final int MAX_AGE = 100;

    public DateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth != null) {
            int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
            if (age < MIN_AGE || age > MAX_AGE) {
                throw new CustomerAgeOutOfRangeException(MIN_AGE, MAX_AGE);
            }
        }
        this.value = dateOfBirth;
    }

    public static DateOfBirth from(LocalDate dateOfBirth) {
        return new DateOfBirth(dateOfBirth);
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isBirthdayToday() {
        if (value == null) return false;
        LocalDate today = LocalDate.now();
        return value.getMonth() == today.getMonth()
                && value.getDayOfMonth() == today.getDayOfMonth();
    }

    public LocalDate getValue() {
        return value;
    }

    public static DateOfBirth empty() {
        return new DateOfBirth(null);
    }
}

