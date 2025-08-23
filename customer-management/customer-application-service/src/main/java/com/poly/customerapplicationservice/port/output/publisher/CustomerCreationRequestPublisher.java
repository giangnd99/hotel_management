package com.poly.customerapplicationservice.port.output.publisher;

import com.poly.customerapplicationservice.message.CustomerBookingMessage;

public interface CustomerCreationRequestPublisher {

    void publish(CustomerBookingMessage message);
}
