package com.poly.customerdomain.model.exception;

import com.poly.domain.exception.DomainException;

public class CustomerAgeOutOfRangeException extends DomainException {
    public CustomerAgeOutOfRangeException(int minAge, int maxAge) {
        super("Tuổi phải nằm trong khoảng từ " + minAge + " đến " + 100 + ".");
    }
}
