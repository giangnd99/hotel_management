package com.poly.customerdomain.model.exception;

import com.poly.domain.exception.DomainException;

public class NameInvalidException extends DomainException {

    public NameInvalidException() {
        super("Tên không hợp lệ!");
    }
}
