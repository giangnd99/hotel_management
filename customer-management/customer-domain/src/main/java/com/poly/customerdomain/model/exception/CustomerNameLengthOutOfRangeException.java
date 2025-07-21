package com.poly.customerdomain.model.exception;

public class CustomerNameLengthOutOfRangeException extends DomainException {
    public CustomerNameLengthOutOfRangeException(int min, int max) {
        super("Tên khách hàng phải có độ dài từ %s đến %s ký tự.".formatted(min, max));
    }

    @Override
    public String getErrorCode() {
        return "CUSTOMER_NAME_OUT_OF_RANGE";
    }
}
