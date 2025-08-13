package com.poly.paymentdomain.model.entity.value_object;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class InvoiceVoucherId extends BaseId<UUID> {
    protected InvoiceVoucherId(UUID value) {
        super(value);
    }

    public static InvoiceVoucherId from(UUID value) {
        return new InvoiceVoucherId(value);
    }

    public static InvoiceVoucherId generate() {
        return new InvoiceVoucherId(UUID.randomUUID());
    }
}
