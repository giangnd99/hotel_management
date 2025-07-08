package com.poly.customerapplicationservice.exception;

public abstract class ApplicationServiceException extends RuntimeException {
    public ApplicationServiceException(String message) {
        super(message);
    }
    public abstract String getErrorCode();
}
