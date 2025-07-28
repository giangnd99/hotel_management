package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;

public class PaymentMethod {

    private final Status value;

    public PaymentMethod(Status value) {
        if (value == null) throw new InvalidValueException(null, "status", "PaymentMethod");
        this.value = value;
    }

    public PaymentMethod from(String value) {
        return new PaymentMethod(PaymentMethod.Status.valueOf(value));
    }

    public String to(PaymentMethod paymentMethod) {
        return paymentMethod.value.toString();
    }

    public Status getValue() {
        return value;
    }

    public enum Status {
        CASH, PAYOS
    }
}