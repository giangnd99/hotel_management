package com.poly.notification.management.command;

import com.poly.notification.management.message.NotificationMessage;

public interface SendBookingConfirmCommand {

    void sendEmailConfirm(NotificationMessage message);
}
