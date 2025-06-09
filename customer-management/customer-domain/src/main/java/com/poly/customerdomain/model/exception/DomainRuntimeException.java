package com.poly.customerdomain.model.exception;

public abstract class DomainRuntimeException extends RuntimeException {

    private ErrorDomainCode errorDomainCode;

    public DomainRuntimeException(ErrorDomainCode code) {
        super(code.getMessage());
        this.errorDomainCode = code;
    }

    public ErrorDomainCode getErrorDomainCode() {
        return errorDomainCode;
    }
}
