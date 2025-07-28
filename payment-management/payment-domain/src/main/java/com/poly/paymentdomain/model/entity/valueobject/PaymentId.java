package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PaymentId {

    private final UUID value;

    public PaymentId(UUID value) {
        if (value == null) throw new InvalidValueException("PaymentId");
        this.value = value;
    }

    public static PaymentId from(UUID value) {
        return new PaymentId(value);
    }

    public static UUID to(PaymentId paymentId) {
        return paymentId.value;
    }

    public String getValue() {
        return value.toString();
    }
}
