package com.poly.servicemanagement.messaging.publisher;

import com.poly.servicemanagement.messaging.message.ServiceOrderRequestMessage;

public interface ServiceOrderRequestPublisher {
    void publish(ServiceOrderRequestMessage serviceOrderRequestMessage);
}
