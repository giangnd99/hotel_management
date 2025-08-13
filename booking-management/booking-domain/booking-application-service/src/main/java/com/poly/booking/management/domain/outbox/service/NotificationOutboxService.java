package com.poly.booking.management.domain.outbox.service;

import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.booking.management.domain.outbox.payload.NotifiEventPayload;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationOutboxService {
    void save(NotifiOutboxMessage notificationOutboxMessage);

    Optional<NotifiOutboxMessage> getBySagaIdAndSagaStatus(
            UUID sagaId,
            SagaStatus... sagaStatus);

    Optional<List<NotifiOutboxMessage>> getListByBookingIdAndStatus(OutboxStatus outboxStatus,
                                                                    SagaStatus... sagaStatus);

    NotifiOutboxMessage getUpdated(NotifiOutboxMessage notifiOutboxMessage,
                                   BookingStatus status,
                                   SagaStatus sagaStatus);

    void saveWithPayloadAndBookingStatusAndSagaStatusAndOutboxStatusAndSagaId
            (NotifiEventPayload payload,
             BookingStatus status,
             SagaStatus sagaStatus,
             OutboxStatus outboxStatus,
             UUID uuid);

    void deleteByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                           SagaStatus... sagaStatus);
}
