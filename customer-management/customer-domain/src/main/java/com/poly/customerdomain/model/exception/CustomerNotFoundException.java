package com.poly.customerdomain.model.exception;

import com.poly.domain.valueobject.CustomerId;

import java.util.UUID;

public class CustomerNotFoundException extends DomainException{
    public CustomerNotFoundException(UUID userId) {
        super("Customer with id  %s not found".formatted(userId));
    }
    public CustomerNotFoundException(CustomerId customerId) {
        super("Customer with id %s not found".formatted(customerId.getValue()));
    }
    @Override
    public String getErrorCode() {
        return "CUSTOMER_NOT_FOUND";
    }
}
