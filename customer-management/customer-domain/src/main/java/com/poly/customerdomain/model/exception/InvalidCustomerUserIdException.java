package com.poly.customerdomain.model.exception;

public class InvalidCustomerUserIdException extends DomainException{
    public InvalidCustomerUserIdException() {
        super("User Id dùng không hợp lệ.");
    }
    @Override
    public String getErrorCode() {
        return "INVALID_CUSTOMER_USER_ID";
    }
}
