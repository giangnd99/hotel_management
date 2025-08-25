package com.poly.notification.management.kafka.adaptor;

import com.poly.notification.management.command.SendBookingConfirmCommand;
import com.poly.notification.management.message.NotificationMessage;
import com.poly.notification.management.port.in.listener.BookingConfirmEmailRequestListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingConfirmEmailRequestListenerImpl implements BookingConfirmEmailRequestListener {

    private final SendBookingConfirmCommand sendBookingConfirmCommand;

    @Override
    public void onBookingConfirmEmailRequest(NotificationMessage message) {
        sendBookingConfirmCommand.sendEmailConfirm(message);
    }

    @Override
    public void onBookingCancelEmailRequest(NotificationMessage message) {

    }
}
