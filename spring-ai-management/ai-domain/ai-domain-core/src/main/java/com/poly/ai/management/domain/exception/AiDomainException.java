package com.poly.ai.management.domain.exception;

import com.poly.domain.exception.DomainException;

public class AiDomainException extends DomainException {

    public AiDomainException(String message) {
        super(message);
    }

    public AiDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
