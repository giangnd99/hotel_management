package com.poly.notification.management.port.out.publisher;

import com.poly.notification.management.message.NotificationMessage;

public interface BookingCancelEmailResponsePublisher {

    void publish(NotificationMessage message);
}
