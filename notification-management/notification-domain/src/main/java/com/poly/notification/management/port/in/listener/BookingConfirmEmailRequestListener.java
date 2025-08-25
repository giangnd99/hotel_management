package com.poly.notification.management.port.in.listener;

import com.poly.notification.management.message.NotificationMessage;

public interface BookingConfirmEmailRequestListener {

    void onBookingConfirmEmailRequest(NotificationMessage message);

    void onBookingCancelEmailRequest(NotificationMessage message);
}
