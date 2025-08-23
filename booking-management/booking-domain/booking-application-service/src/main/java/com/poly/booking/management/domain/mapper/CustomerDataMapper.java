package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.message.reponse.CustomerCreatedMessageResponse;
import com.poly.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerDataMapper {

    /**
     * Chuyển đổi CustomerCreatedMessageResponse thành Customer Entity
     * <p>
     * Mục đích: Tạo customer entity từ message response khi nhận customer created event
     * Sử dụng: Trong CustomerListenerImpl để xử lý customer created event
     *
     * @param customerCreatedEvent CustomerCreatedMessageResponse từ Kafka message
     * @return Customer entity
     */
    public Customer customerCreatedEventToCustomer(CustomerCreatedMessageResponse customerCreatedEvent) {
        return Customer.Builder.builder()
                .name(customerCreatedEvent.getLastName().concat(" ").concat(customerCreatedEvent.getFirstName()))
                .email(customerCreatedEvent.getUsername())
                .username(customerCreatedEvent.getUsername())
                .id(new CustomerId(UUID.fromString(customerCreatedEvent.getCustomerId())))
                .lastName(customerCreatedEvent.getLastName())
                .firstName(customerCreatedEvent.getFirstName())
                .status(customerCreatedEvent.isActive() ? Customer.CustomerStatus.ACTIVE : Customer.CustomerStatus.INACTIVE)
                .build();
    }
}
