package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;

import java.util.concurrent.ThreadLocalRandom;

public class PaymentReference {

    private final String value;

    public PaymentReference(String value) {
        if (value == null) throw new InvalidValueException("PaymentReference");
        this.value = value;
    }

    public static PaymentReference generate() {
        long code = System.currentTimeMillis() * 1000 + ThreadLocalRandom.current().nextInt(1000);
        return new PaymentReference(String.valueOf(code));
    }

    public static PaymentReference from(String value) {
        return new PaymentReference(value);
    }

    public static String to(PaymentReference paymentReference) {
        return paymentReference.getValue();
    }

    public String getValue() {
        return value.toString();
    }

    public static PaymentReference empty() {
        return  new PaymentReference("Empty PaymentReference");
    }
}
