package com.poly.paymentdomain.model.entity.value_object;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class InvoiceServiceId extends BaseId<UUID> {
    protected InvoiceServiceId(UUID value) {
        super(value);
    }

    public static InvoiceServiceId generate() {
        return new InvoiceServiceId(UUID.randomUUID());
    }

    public static InvoiceServiceId from(UUID value) {
        return new InvoiceServiceId(value);
    }
}
