package edu.poly.servicemanagement.messaging.publisher;

import edu.poly.servicemanagement.messaging.message.ServiceOrderRequestMessage;

public interface ServiceOrderRequestPublisher {
    void publish(ServiceOrderRequestMessage serviceOrderRequestMessage);
}
