package com.poly.booking.management.domain.port.out.message.publisher;

import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface NotificationRequestMessagePublisher {

    void sendNotifi(NotifiOutboxMessage notifiOutboxMessage,
                    BiConsumer<NotifiOutboxMessage, OutboxStatus> outboxCallback);

    void sendNotifiCancel(NotifiOutboxMessage notifiOutboxMessage,
                          BiConsumer<NotifiOutboxMessage, OutboxStatus> outboxCallback);
}
