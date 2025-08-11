package com.poly.booking.management.domain.valueobject;

import com.poly.domain.valueobject.*;

import java.util.UUID;

public class BookingRoomId extends BaseId<UUID> {

    public BookingRoomId(UUID value) {
        super(value);
    }
}
