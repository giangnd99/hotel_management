package com.poly.customerdomain.model.exception;

public class BlankCustomerNameException extends DomainException {
    public BlankCustomerNameException() {
        super("Họ và tên khách hàng không được để trống.");
    }

    @Override
    public String getErrorCode() {
        return "CUSTOMER_NAME_BLANK";
    }
}
