package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;

import java.util.UUID;

public class ServiceId {
    private final UUID value;

    public ServiceId(UUID value) {
        if (value == null) throw new InvalidValueException("ServiceId");
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }
}
