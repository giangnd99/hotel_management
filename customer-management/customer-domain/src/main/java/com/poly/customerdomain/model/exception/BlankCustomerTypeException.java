package com.poly.customerdomain.model.exception;

import com.poly.domain.exception.DomainException;

public class BlankCustomerTypeException extends DomainException {
    public BlankCustomerTypeException() {
        super("Loại khách hàng không được để trống.");
    }
}
