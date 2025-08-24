package com.poly.domain.valueobject;

import java.util.UUID;

public class InvoiceId extends BaseId<UUID> {

    public InvoiceId(UUID value) {
        super(value);
    }

    public static InvoiceId generate() {
        return new InvoiceId(UUID.randomUUID());
    }

    public static InvoiceId from(UUID entity) {
        return new InvoiceId(entity);
    }

}