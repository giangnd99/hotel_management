package com.poly.booking.management.domain.outbox.service;

import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.payload.ReservedEventPayload;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomOutboxService {

    void save(RoomOutboxMessage roomOutboxMessage);

    Optional<RoomOutboxMessage> getRoomOutboxMessageBySagaIdAndSagaStatus(
            UUID sagaId,
            SagaStatus... sagaStatus);

    Optional<List<RoomOutboxMessage>> getRoomOutboxMessageByBookingIdAndStatus(OutboxStatus outboxStatus,
                                                                               SagaStatus... sagaStatus);

    RoomOutboxMessage getUpdatedRoomOutBoxMessage(RoomOutboxMessage roomOutboxMessage,
                                                  BookingStatus status,
                                                  SagaStatus sagaStatus);

    void saveRoomOutboxMessage(ReservedEventPayload reservedEventPayload,
                               BookingStatus status,
                               SagaStatus sagaStatus,
                               OutboxStatus outboxStatus,
                               UUID uuid);

    void deleteRoomOutboxMessageByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                                            SagaStatus... sagaStatus);
}
