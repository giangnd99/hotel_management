package com.poly.customerdomain.model.valueobject;

import com.poly.customerdomain.model.exception.CustomerDomainException;
import com.poly.customerdomain.model.exception.ErrorDomainCode;

public record Name(String fullName) {

    public Name {
        if (fullName == null || fullName.isBlank()) {
            throw new CustomerDomainException(ErrorDomainCode.NAME_INVALID);
        }
    }

}