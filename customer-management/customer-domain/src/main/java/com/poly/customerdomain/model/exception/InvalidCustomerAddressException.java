package com.poly.customerdomain.model.exception;

public class InvalidCustomerAddressException extends DomainException {
    public InvalidCustomerAddressException() {
        super("Thông tin địa chỉ không hơp lệ.");
    }
    @Override
    public String getErrorCode() {
        return "INVALID_CUSTOMER_ADDRESS";
    }
}
