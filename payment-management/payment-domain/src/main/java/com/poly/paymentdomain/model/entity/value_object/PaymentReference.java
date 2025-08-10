package com.poly.paymentdomain.model.entity.value_object;

import java.util.concurrent.ThreadLocalRandom;

public class PaymentReference {

    private final long value;

    public PaymentReference(long value) {
        this.value = value;
    }

    public static PaymentReference generate() {
        long code = System.currentTimeMillis() * 1000 + ThreadLocalRandom.current().nextInt(1000);
        return new PaymentReference(code);
    }

    public static PaymentReference from(long value) {
        return new PaymentReference(value);
    }

    public static long to(PaymentReference paymentReference) {
        return paymentReference.getValue();
    }

    public long getValue() {
        return value;
    }

    public static PaymentReference empty() {
        return  new PaymentReference(00000);
    }
}
