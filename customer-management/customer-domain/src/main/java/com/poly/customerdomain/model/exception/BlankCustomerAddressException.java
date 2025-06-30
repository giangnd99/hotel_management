package com.poly.customerdomain.model.exception;

import com.poly.domain.exception.DomainException;

public class BlankCustomerAddressException extends DomainException {
    public BlankCustomerAddressException() {
        super("Địa chỉ không được để trống bất kỳ trường nào.");
    }
}
