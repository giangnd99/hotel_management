package com.poly.customerapplicationservice.exception;

public abstract class ApplicationServiceException extends RuntimeException {
    public ApplicationServiceException(String message) {
        super(message);
    }
    public ApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public abstract String getErrorCode();
}
