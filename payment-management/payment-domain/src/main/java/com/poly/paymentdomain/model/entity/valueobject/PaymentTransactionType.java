package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;

public class PaymentTransactionType {

    private final Status value;

    public static final PaymentTransactionType DEPOSIT = new PaymentTransactionType(PaymentTransactionType.Status.DEPOSIT);

    public static final PaymentTransactionType OTHER = new PaymentTransactionType(PaymentTransactionType.Status.OTHER);

    public static final PaymentTransactionType INVOICE_PAYMENT = new PaymentTransactionType(PaymentTransactionType.Status.INVOICE_PAYMENT);

    public static final PaymentTransactionType REFUND = new PaymentTransactionType(PaymentTransactionType.Status.REFUND);

    public PaymentTransactionType(Status value) {
        if (value == null) throw new InvalidValueException(null, "status", "PaymentTransactionType");
        this.value = value;
    }

    public String getValue() {
        return value.toString();
    }

    public static PaymentTransactionType from(String value) {
        return new PaymentTransactionType(Status.valueOf(value));
    }

    public String to(PaymentTransactionType serviceType) {
        return serviceType.value.toString();
    }

    public enum Status {
        DEPOSIT,
        INVOICE_PAYMENT,
        REFUND,
        OTHER
    }
}
