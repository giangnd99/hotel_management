package com.poly.customerdomain.model.exception;

import com.poly.domain.exception.DomainException;

public class BlankCustomerNameException extends DomainException {
    public BlankCustomerNameException() {
        super("Tên khách hàng không được để trống hoặc chỉ chứa khoảng trắng.");
    }
}
