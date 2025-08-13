package com.poly.paymentdomain.model.entity.value_object;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class InvoiceBookingId extends BaseId<UUID> {
    protected InvoiceBookingId(UUID value) {
        super(value);
    }

    public static InvoiceBookingId from(UUID value) {
        return new InvoiceBookingId(value);
    }

    public static InvoiceBookingId generate() {
        return new InvoiceBookingId(UUID.randomUUID());
    }
}
