package com.poly.paymentdomain.model.value_object;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PaymentId {

    private final UUID value;

    public PaymentId(UUID value) {
        this.value = value;
    }

    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID());
    }

    public static PaymentId from(UUID value) {
        return new PaymentId(value);
    }

    public static UUID to(PaymentId paymentId) {
        return paymentId.value;
    }

    public UUID getValue() {
        return value;
    }
}
