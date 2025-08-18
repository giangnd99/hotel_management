package com.poly.notification.management.port.out.publisher;

import com.poly.notification.management.message.NotificationMessage;

public interface BookingConfirmedEmailResponsePublisher {

    void publish(NotificationMessage notification);
}
