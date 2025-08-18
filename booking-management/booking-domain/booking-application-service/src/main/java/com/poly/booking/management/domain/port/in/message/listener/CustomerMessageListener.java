package com.poly.booking.management.domain.port.in.message.listener;


import com.poly.booking.management.domain.message.CustomerCreatedMessageResponse;

public interface CustomerMessageListener {

    void customerCreated(CustomerCreatedMessageResponse customerCreatedEvent);
}
