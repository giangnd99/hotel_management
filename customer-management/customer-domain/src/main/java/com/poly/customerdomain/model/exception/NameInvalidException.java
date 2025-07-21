package com.poly.customerdomain.model.exception;

public class NameInvalidException extends DomainException {

    public NameInvalidException() {
        super("Tên không hợp lệ!");
    }

    @Override
    public String getErrorCode() {
        return "CUSTOMER_NAME_INVALID";
    }
}
