package com.poly.booking.management.domain.port.in.message.listener;


import com.poly.booking.management.domain.message.RoomMessageResponse;

public interface RoomReservedListener {

    void roomReserved(RoomMessageResponse roomReservedResponseMessage);

    void roomReservationCancelled(RoomMessageResponse roomReservedResponseMessage);
}
