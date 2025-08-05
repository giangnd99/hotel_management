package com.poly.booking.management.domain.port.out.message.publisher.notification;

import com.poly.booking.management.domain.outbox.model.notification.BookingNotifiOutboxMessage;
import com.poly.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface BookingConfirmedRequestMessagePublisher {

    void sendConfirmedQrCode(BookingNotifiOutboxMessage bookingNotifiOutboxMessage,
                             BiConsumer<BookingNotifiOutboxMessage, OutboxStatus> outboxCallback);
}
