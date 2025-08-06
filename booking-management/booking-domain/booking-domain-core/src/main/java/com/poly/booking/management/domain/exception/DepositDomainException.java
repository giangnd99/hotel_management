package com.poly.booking.management.domain.exception;

import com.poly.domain.exception.DomainException;

public class DepositDomainException extends DomainException {
    public DepositDomainException(String message) {
        super(message);
    }
    public DepositDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
