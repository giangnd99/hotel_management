package com.poly.notification.management.command;

import com.poly.notification.management.message.NotificationMessage;

public interface SendBookingCancelCommand {

    void sendEmailCancel(NotificationMessage message);
}
