package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;

public class InvoiceStatus {

    private final Status value;

    public static InvoiceStatus DRAFT = new InvoiceStatus(Status.DRAFT);

    public static InvoiceStatus PENDING = new InvoiceStatus(Status.PENDING);

    public static InvoiceStatus PAID = new InvoiceStatus(Status.PAID);

    public static InvoiceStatus FINALIZED = new InvoiceStatus(Status.FINALIZED);

    public static InvoiceStatus CANCELED = new InvoiceStatus(Status.CANCELED);

    public InvoiceStatus(Status value) {
        if (value == null) throw new InvalidValueException(null, "status", "InvoiceStatus");
        this.value = value;
    }

    public boolean isDraft() {
        return value == Status.DRAFT;
    }

//    public boolean canTransitionTo(Status newStatus) {
//        return switch (value) {
//            case PENDING-> newStatus == Status.PAID || newStatus == Status.CANCELED;
//            case PAID ->  newStatus == Status.CANCELED;
//            case DRAFT, CANCELED -> false;
//        };
//    }

    public enum Status {
        DRAFT, PENDING, PAID, FINALIZED, CANCELED
    }

    public static InvoiceStatus from(String value) {
        return new InvoiceStatus(Status.valueOf(value));
    }

    public static InvoiceStatus paidStatus() {
        return new InvoiceStatus(Status.PAID);
    }

    public Status getValue() {
        return value;
    }
}
