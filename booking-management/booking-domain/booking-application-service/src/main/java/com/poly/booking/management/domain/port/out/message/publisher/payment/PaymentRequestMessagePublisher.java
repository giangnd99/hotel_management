package com.poly.booking.management.domain.port.out.message.publisher.payment;

import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentOutboxMessage;
import com.poly.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface PaymentRequestMessagePublisher {

    void sendTotalBookingAmount(BookingPaymentOutboxMessage paymentOutboxMessage,
                                BiConsumer<BookingPaymentOutboxMessage, OutboxStatus> outboxCallback);
}
