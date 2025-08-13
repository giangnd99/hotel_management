package com.poly.booking.management.domain.valueobject;

import com.poly.domain.valueobject.ReferenceId;
import com.poly.domain.valueobject.CompositeId;
import com.poly.domain.valueobject.CompositeKey;
import com.poly.domain.valueobject.RoomId;

public class BookingRoomId extends CompositeId<ReferenceId, RoomId> {

    public BookingRoomId(CompositeKey<ReferenceId, RoomId> value) {
        super(value);
    }
}
