package com.poly.customerdomain.model.exception;

public class CustomerDomainException extends DomainRuntimeException{

    public CustomerDomainException(ErrorDomainCode code) {
        super(code);
    }

}
