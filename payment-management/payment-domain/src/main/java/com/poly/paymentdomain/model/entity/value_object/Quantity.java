package com.poly.paymentdomain.model.entity.value_object;

import com.poly.paymentdomain.model.exception.InvalidValueException;

public class Quantity {

    private Integer value;

    public Quantity(Integer value) {
        if (value == null) throw new InvalidValueException("Quantity");
        this.value = value;
    }

    public static Quantity from(Integer value) {
        return new Quantity(value);
    }

    public static Integer to(Quantity quantity) {
        return quantity.value;
    }

    public Integer getValue() {
        return value;
    }
}
