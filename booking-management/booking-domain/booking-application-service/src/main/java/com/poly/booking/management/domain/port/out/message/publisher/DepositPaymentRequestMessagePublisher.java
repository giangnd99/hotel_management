package com.poly.booking.management.domain.port.out.message.publisher;

import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;

public interface DepositPaymentRequestMessagePublisher {

    void sendDepositRequest(PaymentOutboxMessage paymentOutboxMessage);
}
