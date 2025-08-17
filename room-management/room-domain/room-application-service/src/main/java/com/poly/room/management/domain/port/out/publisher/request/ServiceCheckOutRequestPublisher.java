package com.poly.room.management.domain.port.out.publisher.request;

import com.poly.message.model.service.ServiceRequestMessage;

public interface ServiceCheckOutRequestPublisher {

    void publish(ServiceRequestMessage serviceRequestMessage);
}
