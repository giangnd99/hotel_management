package com.poly.paymentapplicationservice.exception;

public class ApplicationServiceException extends RuntimeException {
    public ApplicationServiceException(String message) {
        super(message);
    }
    public ApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
