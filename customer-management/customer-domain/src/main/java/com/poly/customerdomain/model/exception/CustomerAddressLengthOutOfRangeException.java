package com.poly.customerdomain.model.exception;

public class CustomerAddressLengthOutOfRangeException extends DomainException {
    public CustomerAddressLengthOutOfRangeException(int  min, int max) {
        super("Địa chỉ khách hàng phải có độ dài từ %s đến %s ký tự.".formatted(min, max));
    }

    @Override
    public String getErrorCode() {
    return "CUSTOMER_ADDRESS_LENGTH_OUT_OF_RANGE";
    }
}
