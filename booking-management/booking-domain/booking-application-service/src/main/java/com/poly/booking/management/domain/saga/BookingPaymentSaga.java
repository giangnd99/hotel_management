package com.poly.booking.management.domain.saga;

import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentOutboxMessage;
import com.poly.booking.management.domain.port.in.message.listener.payment.PaymentResponseMessageListener;
import com.poly.saga.SagaStep;

import java.util.Optional;

public class BookingPaymentSaga implements SagaStep<PaymentResponseMessageListener> {

    @Override
    public void process(PaymentResponseMessageListener data) {
        Optional<BookingPaymentOutboxMessage>
    }

    @Override
    public void rollback(PaymentResponseMessageListener data) {

    }
}
