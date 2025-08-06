package com.poly.booking.management.domain.port.out.repository;

import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentOutboxMessage;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;

public interface PaymentOutBoxRepository {
    BookingPaymentOutboxMessage save(BookingPaymentOutboxMessage updatePaymentOutboxMessage);

    void deleteByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus... sagaStatus);
}
