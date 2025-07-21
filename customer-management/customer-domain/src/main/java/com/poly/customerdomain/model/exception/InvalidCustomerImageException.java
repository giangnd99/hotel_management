package com.poly.customerdomain.model.exception;

public class InvalidCustomerImageException extends DomainException {
    public InvalidCustomerImageException() {
        super("Ảnh không hợp lệ hoặc đang trống.");
    }
    @Override
    public String getErrorCode() {
        return "INVALID_CUSTOMER_IMAGE";
    }
}
