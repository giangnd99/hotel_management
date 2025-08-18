package com.poly.restaurant.application.port.out.message.publisher;

import com.poly.message.model.room.RoomRequestMessage;

public interface RoomRequestPublisher {
    void publish(RoomRequestMessage roomRequestMessage);
}
