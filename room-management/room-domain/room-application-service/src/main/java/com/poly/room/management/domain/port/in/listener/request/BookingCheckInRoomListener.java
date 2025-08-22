package com.poly.room.management.domain.port.in.listener.request;

import com.poly.room.management.domain.message.BookingRoomRequestMessage;

public interface BookingCheckInRoomListener {

    void onCheckInRoom(BookingRoomRequestMessage roomRequestMessage);
}
