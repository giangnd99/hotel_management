package com.poly.payment.management.domain.value_object;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class InvoicePaymentId extends BaseId<UUID> {
    protected InvoicePaymentId(UUID value) {
        super(value);
    }

    public static InvoicePaymentId generate() {
        return new InvoicePaymentId(UUID.randomUUID());
    }

    public static InvoicePaymentId from(UUID value) {
        return new InvoicePaymentId(value);
    }
}
