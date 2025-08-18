package com.poly.booking.management.domain.port.out.repository;

import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentOutBoxRepository {
    PaymentOutboxMessage save(PaymentOutboxMessage updatePaymentOutboxMessage);

    Optional<PaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String bookingSagaName, UUID sagaId, SagaStatus... sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus... sagaStatus);

    Optional<List<PaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus... sagaStatus);
}
