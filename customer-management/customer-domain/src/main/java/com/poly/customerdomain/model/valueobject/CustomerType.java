package com.poly.customerdomain.model.valueobject;

import com.poly.customerdomain.model.exception.BlankCustomerTypeException;

public class                                CustomerType {

    private final Type type;

    public enum Type {
        REGULAR, PREMIUM, VIP
    }

    private CustomerType(Type type) {
        if (type == null) {
            throw new BlankCustomerTypeException();
        }
        this.type = type;
    }

    public static CustomerType regular() {
        return new CustomerType(Type.REGULAR);
    }

    public static CustomerType from(Type type) {
        if (type == null) {
            throw new BlankCustomerTypeException();
        }

        if (type != Type.REGULAR && type != Type.PREMIUM) {
            throw new UnsupportedOperationException("Type " + type + " is not allowed to be set directly");
        }
        return new CustomerType(type);
    }

    public Type getType() {
        return type;
    }

}
