package com.poly.domain.valueobject;

import java.util.UUID;

public class BookingId extends BaseId<UUID> {
    public BookingId(UUID id) {
        super(id);
    }
}
