package com.poly.booking.management.domain.outbox.scheduler.payment;

import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentOutboxMessage;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.EBookingStatus;
import com.poly.saga.SagaStatus;

import java.util.Optional;
import java.util.UUID;

public class PaymentOutboxHelper {
    public Optional<BookingPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID uuid, SagaStatus... statuses) {
        return Optional.empty();
    }

    public void save(BookingPaymentOutboxMessage updatePaymentOutboxMessage) {
    }

    public BookingPaymentOutboxMessage getUpdatePaymentOutboxMessage(BookingPaymentOutboxMessage bookingPaymentOutboxMessage, EBookingStatus status, SagaStatus sagaStatus) {

        bookingPaymentOutboxMessage.setProcessedAt(DateCustom.now());
        bookingPaymentOutboxMessage.setBookingStatus(status);
        bookingPaymentOutboxMessage.setSagaStatus(sagaStatus);
        return bookingPaymentOutboxMessage;
    }
}
