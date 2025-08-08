package com.poly.booking.management.domain.port.in.message.listener.room;

import com.poly.booking.management.domain.dto.message.RoomMessageResponse;

public interface RoomReservedListener {

    void roomReserved(RoomMessageResponse roomReservedResponseMessage);

    void roomReservationCancelled(RoomMessageResponse roomReservedResponseMessage);
}
