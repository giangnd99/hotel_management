package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.messaging.message.CustomerCreatedMessageResponse;
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
        return Customer.createCustomer(
                UUID.fromString(customerCreatedEvent.getId()),
                customerCreatedEvent.getUsername(),
                customerCreatedEvent.getFirstName(),
                customerCreatedEvent.getLastName()
        );
    }
}
