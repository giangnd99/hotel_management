package com.poly.room.management.domain.port.out.publisher.reponse;

import com.poly.room.management.domain.message.BookingRoomResponseMessage;

public interface BookingRoomReservePublisher {

    void publish(BookingRoomResponseMessage bookingRoomResponseMessage);
}
