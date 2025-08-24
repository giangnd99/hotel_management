package com.poly.booking.management.domain.port.out.repository;

import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationOutboxRepository {
    NotifiOutboxMessage save(NotifiOutboxMessage notifiOutboxMessage);

    void deleteByTypeAndOutboxStatusAndSagaStatus(OutboxStatus outboxStatus, SagaStatus... status);

    Optional<NotifiOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String bookingSagaName, UUID sagaId, SagaStatus... status);

    Optional<List<NotifiOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus... status);
}
