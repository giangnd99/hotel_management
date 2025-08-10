package com.poly.paymentdomain.model.entity.valueobject2;

import com.poly.paymentdomain.model.exception.InvalidValueException;

public class ServiceType {

    private final Status value;

    public ServiceType(Status value) {
        if (value == null) throw new InvalidValueException(null, "status", "ServiceType");
        this.value = value;
    }

    public Status getValue() {
        return value;
    }

    public static ServiceType from(String value) {
        return new ServiceType(Status.valueOf(value));
    }

    public String to(ServiceType serviceType) {
        return serviceType.value.toString();
    }

    public enum Status {
        ROOM, FOOD, SPA, OTHER
    }
}