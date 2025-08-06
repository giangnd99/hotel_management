package com.poly.booking.management.domain.port.out.repository;

import com.poly.booking.management.domain.outbox.model.room.BookingRoomOutboxMessage;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomReserveOutBoxRepository {
    BookingRoomOutboxMessage save(BookingRoomOutboxMessage bookingRoomOutboxMessage);

    Optional<BookingRoomOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String bookingSagaName, UUID sagaId, SagaStatus[] statuses);

    Optional<List<BookingRoomOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus[] sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus[] sagaStatus);
}
