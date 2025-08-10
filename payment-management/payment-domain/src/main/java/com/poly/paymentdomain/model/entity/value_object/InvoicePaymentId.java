package com.poly.paymentdomain.model.entity.value_object;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class InvoicePaymentId extends BaseId<UUID> {
    protected InvoicePaymentId(UUID value) {
        super(value);
    }
}
