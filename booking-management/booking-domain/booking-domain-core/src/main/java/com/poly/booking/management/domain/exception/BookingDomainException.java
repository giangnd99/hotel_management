package com.poly.booking.management.domain.exception;

import com.poly.domain.exception.DomainException;

public class BookingDomainException extends DomainException {
    public BookingDomainException(String message) {
        super(message);
    }

    public BookingDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
