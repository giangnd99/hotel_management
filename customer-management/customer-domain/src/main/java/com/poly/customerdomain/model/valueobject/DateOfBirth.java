package com.poly.customerdomain.model.valueobject;

import com.poly.customerdomain.model.exception.CustomerAgeOutOfRangeException;

import java.time.LocalDate;
import java.time.Period;

public class DateOfBirth{

    private LocalDate dateOfBirth;

    public DateOfBirth(LocalDate dateOfBirth) {
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        if (age < 15 || age > 100) {
            throw new CustomerAgeOutOfRangeException(15, 100);
        }
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isBirthdayToday() {
        LocalDate today = LocalDate.now();
        return dateOfBirth.getMonth() == today.getMonth()
                && dateOfBirth.getDayOfMonth() == today.getDayOfMonth();
    }
}
