package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;

public class PaymentMethod {

    private final Status value;

    public PaymentMethod(Status value) {
        if (value == null) throw new InvalidValueException(null, "status", "PaymentMethod");
        this.value = value;
    }

    public String to(PaymentMethod paymentMethod) {
        return paymentMethod.value.toString();
    }

    public String getValue() {
        return value.toString();
    }

    public static PaymentMethod from(String paymentMethod) {
        return new PaymentMethod(PaymentMethod.Status.valueOf(paymentMethod));
    }

    public String getValueString() {
        return value.toString();
    }

    public enum Status {
        CASH, PAYOS
    }
}