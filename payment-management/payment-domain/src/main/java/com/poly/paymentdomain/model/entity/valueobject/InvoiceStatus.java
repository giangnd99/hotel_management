package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;

public class InvoiceStatus {

    private final Status value;

    public InvoiceStatus(Status value) {
        if (value == null) throw new InvalidValueException(null, "status", "InvoiceStatus");
        this.value = value;
    }

    public boolean isDraft() {
        return value == Status.DRAFT;
    }

    public boolean canTransitionTo(Status newStatus) {
        return switch (value) {
            case DRAFT -> newStatus == Status.PAID || newStatus == Status.CANCELED;
            case PAID, CANCELED -> false;
        };
    }

    public enum Status {
        DRAFT, PAID, CANCELED
    }

    public Status getValue() {
        return value;
    }
}
