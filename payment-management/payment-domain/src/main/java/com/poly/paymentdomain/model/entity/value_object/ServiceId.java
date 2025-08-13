package com.poly.paymentdomain.model.entity.value_object;

import com.poly.paymentdomain.model.exception.InvalidValueException;

import java.util.UUID;

public class ServiceId {
    private final UUID value;

    public ServiceId(UUID value) {
        if (value == null) throw new InvalidValueException("ServiceId");
        this.value = value;
    }

    public static ServiceId from(UUID value) {
        return new ServiceId(value);
    }

    public static ServiceId generate() {
        return new ServiceId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }
}
