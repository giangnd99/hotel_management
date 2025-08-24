package com.poly.customerdomain.model.exception;

public class InvalidCustomerNameException extends DomainException {
    public InvalidCustomerNameException() {
        super("Họ tên khách hàng không hợp lệ.");
    }
    @Override
    public String getErrorCode() {
        return "INVALID_CUSTOMER_NAME";
    }
}
