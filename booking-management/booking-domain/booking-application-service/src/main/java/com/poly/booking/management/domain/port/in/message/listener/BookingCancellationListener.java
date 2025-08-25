package com.poly.booking.management.domain.port.in.message.listener;

import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;


public interface BookingCancellationListener {


    void processBookingCancellation(NotificationMessageResponse roomMessageResponse);
}
