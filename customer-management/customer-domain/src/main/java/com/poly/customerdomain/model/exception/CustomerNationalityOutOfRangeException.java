package com.poly.customerdomain.model.exception;

import com.poly.domain.exception.DomainException;

public class CustomerNationalityOutOfRangeException extends DomainException {
    public CustomerNationalityOutOfRangeException(int  min, int max) {
        super("Quốc gia của khách hàng phải nằm trong khoảng từ " + min + " đến " + max + " ký tự.");
    }
}
