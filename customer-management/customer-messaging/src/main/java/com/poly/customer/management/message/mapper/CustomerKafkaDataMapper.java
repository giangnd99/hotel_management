package com.poly.customer.management.message.mapper;

import com.poly.booking.management.domain.kafka.model.CustomerModelAvro;
import com.poly.customerapplicationservice.message.CustomerBookingMessage;
import org.springframework.stereotype.Component;

@Component
public class CustomerKafkaDataMapper {


    public CustomerModelAvro customerModelAvro(CustomerBookingMessage message){
        return CustomerModelAvro.newBuilder()
                .setFirstName(message.getFirstName())
                .setLastName(message.getLastName())
                .setId(message.getCustomerId())
                .setUsername(message.getUsername() == null ? "Customer online" : message.getUsername())
                .setIsActive(message.isActive())
                .build();
    }
}
