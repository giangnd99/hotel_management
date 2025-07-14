package com.poly.customerdataaccess.kafka.listener;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerCreatedEventListener {

    private final CustomerUsecase customerUsecase;

    public CustomerCreatedEventListener(CustomerUsecase customerUsecase) {
        this.customerUsecase = customerUsecase;
    }

    @KafkaListener(topics = "customer-created", groupId = "customer-service")
    public void listen(CreateCustomerCommand command) {
        customerUsecase.initializeCustomerProfile(command);
    }
}
