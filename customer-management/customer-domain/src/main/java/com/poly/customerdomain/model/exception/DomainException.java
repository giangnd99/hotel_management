package com.poly.customerdomain.model.exception;

public abstract class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
    public abstract String getErrorCode();
}