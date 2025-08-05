package com.poly.booking.management.domain.saga.impl;

import com.poly.booking.management.domain.dto.message.CustomerCreatedMessageResponse;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.event.CustomerCreatedEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.port.in.message.listener.customer.CustomerMessageListener;
import com.poly.booking.management.domain.port.out.repository.CustomerRepository;
import com.poly.booking.management.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * CustomerListenerImpl - Implementation of Customer Message Listener
 * 
 * CHỨC NĂNG CHÍNH:
 * - Xử lý customer events từ Kafka messages
 * - Tích hợp với Event Driven Architecture
 * - Quản lý customer lifecycle trong booking system
 * 
 * EVENT DRIVEN FEATURES:
 * - CustomerCreatedEvent: Xử lý khi customer được tạo mới
 * - CustomerUpdatedEvent: Xử lý khi customer được cập nhật
 * - CustomerDeletedEvent: Xử lý khi customer bị xóa
 * 
 * CLEAN CODE PRINCIPLES:
 * - Single Responsibility: Chỉ xử lý customer events
 * - Dependency Injection: Sử dụng constructor injection
 * - Error Handling: Comprehensive exception handling
 * - Logging: Detailed logging cho monitoring
 * 
 * TRANSACTIONAL BEHAVIOR:
 * - @Transactional để đảm bảo data consistency
 * - Rollback tự động khi có exception
 * - Event publishing sau khi transaction commit
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerListenerImpl implements CustomerMessageListener {

    // ==================== DEPENDENCIES ====================

    private final BookingDataMapper bookingDataMapper;
    private final CustomerRepository customerRepository;
    private final CustomerDomainService customerDomainService;

    // ==================== EVENT HANDLING METHODS ====================

    /**
     * Xử lý Customer Created Event
     * 
     * EVENT FLOW:
     * 1. Nhận CustomerCreatedMessageResponse từ Kafka
     * 2. Validate và transform thành Customer entity
     * 3. Lưu customer vào database
     * 4. Publish CustomerCreatedEvent
     * 5. Log kết quả xử lý
     * 
     * ERROR HANDLING:
     * - Validate customer data trước khi lưu
     * - Throw BookingDomainException nếu có lỗi
     * - Rollback transaction tự động
     * 
     * @param customerCreatedEvent CustomerCreatedMessageResponse từ Kafka
     */
    @Override
    @Transactional
    public void customerCreated(CustomerCreatedMessageResponse customerCreatedEvent) {
        log.info("Processing customer created event for customer: {}", customerCreatedEvent.getId());

        try {
            // Step 1: Validate input data
            validateCustomerCreatedEvent(customerCreatedEvent);

            // Step 2: Check if customer already exists
            UUID customerId = UUID.fromString(customerCreatedEvent.getId());
            if (customerRepository.existsById(customerId)) {
                log.warn("Customer already exists with id: {}", customerId);
                return;
            }

            // Step 3: Transform message to domain entity
            Customer customer = bookingDataMapper.customerCreatedEventToCustomer(customerCreatedEvent);

            // Step 4: Validate customer entity
            validateCustomerEntity(customer);

            // Step 5: Save customer using domain service
            Customer savedCustomer = saveCustomerToRepo(customer);

            // Step 6: Log success
            log.info("Customer created successfully with id: {}", savedCustomer.getId().getValue());

        } catch (Exception e) {
            log.error("Error processing customer created event for customer: {}",
                    customerCreatedEvent.getId(), e);
            throw new BookingDomainException("Failed to process customer created event", e);
        }
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Customer saveCustomerToRepo(Customer customer) {
        Customer savedCustomer = customerDomainService.createCustomer(customer);
        if (customerRepository.save(savedCustomer) == null) {
            log.debug("Failed to saved the customer with id: {} in repository domain application",
                    savedCustomer.getId().getValue());
            throw new BookingDomainException(
                    "The Customer can't saved in with id " + savedCustomer.getId().getValue() + " .");
        }
        return savedCustomer;
    }

    /**
     * Validate CustomerCreatedMessageResponse
     * 
     * @param customerCreatedEvent Event cần validate
     * @throws BookingDomainException nếu validation fail
     */
    private void validateCustomerCreatedEvent(CustomerCreatedMessageResponse customerCreatedEvent) {
        if (customerCreatedEvent == null) {
            throw new BookingDomainException("Customer created event cannot be null");
        }

        if (customerCreatedEvent.getId() == null || customerCreatedEvent.getId().trim().isEmpty()) {
            throw new BookingDomainException("Customer ID cannot be null or empty");
        }

        if (customerCreatedEvent.getUsername() == null || customerCreatedEvent.getUsername().trim().isEmpty()) {
            throw new BookingDomainException("Customer username cannot be null or empty");
        }

        if (customerCreatedEvent.getFirstName() == null || customerCreatedEvent.getFirstName().trim().isEmpty()) {
            throw new BookingDomainException("Customer first name cannot be null or empty");
        }

        if (customerCreatedEvent.getLastName() == null || customerCreatedEvent.getLastName().trim().isEmpty()) {
            throw new BookingDomainException("Customer last name cannot be null or empty");
        }

        // Validate UUID format
        try {
            UUID.fromString(customerCreatedEvent.getId());
        } catch (IllegalArgumentException e) {
            throw new BookingDomainException("Invalid customer ID format: " + customerCreatedEvent.getId());
        }

        log.debug("Customer created event validation passed for customer: {}", customerCreatedEvent.getId());
    }

    /**
     * Validate Customer entity
     * 
     * @param customer Customer entity cần validate
     * @throws BookingDomainException nếu validation fail
     */
    private void validateCustomerEntity(Customer customer) {
        if (customer == null) {
            throw new BookingDomainException("Customer entity cannot be null");
        }

        if (customer.getId() == null) {
            throw new BookingDomainException("Customer ID cannot be null");
        }

        if (customer.getUsername() == null || customer.getUsername().trim().isEmpty()) {
            throw new BookingDomainException("Customer username cannot be null or empty");
        }

        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
            throw new BookingDomainException("Customer first name cannot be null or empty");
        }

        if (customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            throw new BookingDomainException("Customer last name cannot be null or empty");
        }

        log.debug("Customer entity validation passed for customer: {}", customer.getId().getValue());
    }
}
