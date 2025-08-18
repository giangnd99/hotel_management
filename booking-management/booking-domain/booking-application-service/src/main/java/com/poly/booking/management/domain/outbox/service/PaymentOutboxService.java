package com.poly.booking.management.domain.outbox.service;

import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.booking.management.domain.outbox.payload.PaymentEventPayload;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentOutboxService {

    void save(PaymentOutboxMessage paymentOutboxMessage);

    Optional<PaymentOutboxMessage> getBySagaIdAndSagaStatus(
            UUID sagaId,
            SagaStatus... sagaStatus);

    Optional<List<PaymentOutboxMessage>> getListByBookingIdAndStatus(OutboxStatus outboxStatus,
                                                                     SagaStatus... sagaStatus);

    void saveWithPayloadAndBookingStatusAndSagaStatusAndOutboxStatusAndSagaId
            (PaymentEventPayload payload,
             BookingStatus status,
             SagaStatus sagaStatus,
             OutboxStatus outboxStatus,
             UUID uuid);

    void deleteByOutboxStatusAndSagaStatus(OutboxStatus outboxStatus,
                                           SagaStatus... sagaStatus);
}
