package com.poly.booking.management.domain.port.in.message.listener.customer;

import com.poly.booking.management.domain.dto.message.CustomerCreatedMessageResponse;

public interface CustomerMessageListener {

    void customerCreated(CustomerCreatedMessageResponse customerCreatedEvent);
}
