package com.poly.booking.management.domain.valueobject;

import com.poly.domain.valueobject.BookingId;
import com.poly.domain.valueobject.CompositeId;
import com.poly.domain.valueobject.CompositeKey;
import com.poly.domain.valueobject.RoomId;

public class BookingRoomId extends CompositeId<BookingId, RoomId> {

    public BookingRoomId(CompositeKey<BookingId, RoomId> value) {
        super(value);
    }
}
