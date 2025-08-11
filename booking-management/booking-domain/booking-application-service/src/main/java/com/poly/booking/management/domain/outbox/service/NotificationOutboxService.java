package com.poly.booking.management.domain.outbox.service;

import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.booking.management.domain.outbox.payload.NotifiEventPayload;
import com.poly.booking.management.domain.outbox.payload.PaymentEventPayload;
import com.poly.domain.valueobject.EBookingStatus;
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
                                   EBookingStatus status,
                                   SagaStatus sagaStatus);

    void saveWithPayloadAndBookingStatusAndSagaStatusAndOutboxStatusAndSagaId
            (NotifiEventPayload payload,
             EBookingStatus status,
             SagaStatus sagaStatus,
             OutboxStatus outboxStatus,
             UUID uuid);

    void deleteByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                           SagaStatus... sagaStatus);
}
