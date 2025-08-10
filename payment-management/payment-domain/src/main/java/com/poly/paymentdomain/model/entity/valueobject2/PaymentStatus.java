package com.poly.paymentdomain.model.entity.valueobject2;

import com.poly.paymentdomain.model.exception.InvalidValueException;

public class PaymentStatus {

    private final Status value;

    public static PaymentStatus PENDING = new PaymentStatus(Status.PENDING);

    public static PaymentStatus COMPLETED = new PaymentStatus(Status.COMPLETED);

    public static PaymentStatus CANCELLED = new PaymentStatus(Status.CANCELLED);

    public static PaymentStatus FAILED = new PaymentStatus(Status.FAILED);

    public static PaymentStatus EXPIRED = new PaymentStatus(Status.EXPIRED);

    public PaymentStatus(Status value) {
        if (value == null) throw new InvalidValueException(null, "status", "PaymentStatus");
        this.value = value;
    }

    public String getValue() {
        return  value.toString();
    }

    public static PaymentStatus from(String value) {
        return new PaymentStatus(Status.valueOf(value));
    }

    public String to(PaymentStatus serviceType) {
        return serviceType.value.toString();
    }

    public enum Status {
        PENDING, COMPLETED, CANCELLED, FAILED, EXPIRED
    }

}
