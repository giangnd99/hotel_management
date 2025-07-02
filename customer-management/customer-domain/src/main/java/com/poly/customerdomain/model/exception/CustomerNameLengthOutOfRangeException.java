package com.poly.customerdomain.model.exception;

import com.poly.domain.exception.DomainException;

public class CustomerNameLengthOutOfRangeException extends DomainException {
    public CustomerNameLengthOutOfRangeException(int min, int max) {
        super("Tên khách hàng phải có độ dài từ " + min + " đến " + max + " ký tự.");
    }
}
