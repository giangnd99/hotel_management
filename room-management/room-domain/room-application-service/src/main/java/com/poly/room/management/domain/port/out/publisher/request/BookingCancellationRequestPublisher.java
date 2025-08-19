package com.poly.room.management.domain.port.out.publisher.request;

import com.poly.room.management.domain.message.RoomCancellationRequestMessage;

public interface BookingCancellationRequestPublisher {

    void publish(RoomCancellationRequestMessage roomCancellationRequestMessage);
}
