package com.poly.customerdomain.model.exception;

public class CustomerAgeOutOfRangeException extends DomainException {
    public CustomerAgeOutOfRangeException(int minAge, int maxAge) {
        super("Tuổi phải nằm trong khoảng từ %s đến %s.".formatted(minAge, maxAge));
    }

    @Override
    public String getErrorCode() {
        return "CUSTOMER_AGE_OUT_OF_RANGE";
    }
}
