package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;

public class PaymentStatus {

    private final Status value;

    public PaymentStatus(Status value) {
        if (value == null) throw new InvalidValueException(null, "status", "PaymentStatus");
        this.value = value;
    }

    public Status getValue() {
        return value;
    }

    public PaymentStatus from(String value) {
        return new PaymentStatus(Status.valueOf(value));
    }

    public String to(PaymentStatus serviceType) {
        return serviceType.value.toString();
    }

    public enum Status {
        COMPLETED, CANCELLED, FAILED
    }

}
