package com.poly.authentication.service.domain.exception;

import com.poly.domain.exception.DomainException;

public class AuthenException extends DomainException {

    public AuthenException(String message) {
        super(message);
    }

    public AuthenException(String message, Throwable cause) {
        super(message, cause);
    }
}
