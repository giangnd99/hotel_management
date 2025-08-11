package com.poly.booking.management.domain.port.out.message.publisher.payment;

import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface DepositPaymentRequestMessagePublisher {

    void sendDepositRequest(PaymentOutboxMessage paymentOutboxMessage,
                            BiConsumer<PaymentOutboxMessage, OutboxStatus> outboxCallback);
}
