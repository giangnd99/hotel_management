package com.poly.booking.management.domain.port.out.repository;

import com.poly.booking.management.domain.outbox.model.notification.BookingNotifiOutboxMessage;

public interface NotificationOutboxRepository {
    BookingNotifiOutboxMessage save(String message);

    void delete(String messageId);

    BookingNotifiOutboxMessage findById(String messageId);

}
