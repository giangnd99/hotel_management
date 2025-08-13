package com.poly.paymentdomain.model.entity.value_object;


public class Quantity {

    private Integer value;

    public Quantity(Integer value) {
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
