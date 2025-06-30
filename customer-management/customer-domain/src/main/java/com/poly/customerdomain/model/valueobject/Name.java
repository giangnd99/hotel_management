package com.poly.customerdomain.model.valueobject;

import com.poly.customerdomain.model.exception.BlankCustomerNameException;
import com.poly.customerdomain.model.exception.CustomerNameLengthOutOfRangeException;
import lombok.Getter;

@Getter
public class Name{

    private String fullName;

    public Name(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new BlankCustomerNameException();
        }
        if (fullName.trim().length() < 5 ||  fullName.trim().length() > 100) {
            throw new CustomerNameLengthOutOfRangeException(5, 100);
        }
        this.fullName = fullName;
    }

}