package com.poly.payment.management.domain.exception;

public class ApplicationServiceException extends RuntimeException {
    public ApplicationServiceException(String message) {
        super(message);
    }
    public ApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
