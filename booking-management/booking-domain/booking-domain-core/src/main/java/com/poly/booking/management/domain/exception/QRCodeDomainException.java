package com.poly.booking.management.domain.exception;

import com.poly.domain.exception.DomainException;

public class QRCodeDomainException extends DomainException {
    public QRCodeDomainException(String message) {
        super(message);
    }
    public QRCodeDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
