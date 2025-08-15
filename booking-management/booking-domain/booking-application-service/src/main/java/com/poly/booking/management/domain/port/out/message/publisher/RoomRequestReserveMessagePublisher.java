package com.poly.booking.management.domain.port.out.message.publisher.room;

import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface RoomRequestReserveMessagePublisher {

    void sendRoomReserveRequest(RoomOutboxMessage roomOutboxMessage,
                                BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback);
}
