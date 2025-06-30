package com.poly.customerdomain.model.exception;

import com.poly.domain.exception.DomainException;

public class CustomerAddressLengthOutOfRangeException extends DomainException {
    public CustomerAddressLengthOutOfRangeException(int  min, int max) {
        super("Địa chỉ khách hàng phải có độ dài từ " + min + " đến " + max + " ký tự.");
    }
}
