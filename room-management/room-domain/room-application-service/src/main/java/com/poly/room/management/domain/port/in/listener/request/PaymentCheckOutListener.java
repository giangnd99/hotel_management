package com.poly.room.management.domain.port.in.listener.request;

import com.poly.message.model.room.RoomRequestMessage;

public interface PaymentCheckOutListener {

    void onPaymentBooking(RoomRequestMessage roomRequestMessage);

}
