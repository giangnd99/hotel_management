package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.event.CustomerCreatedEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.port.out.repository.CustomerRepository;
import com.poly.booking.management.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * CustomerDomainServiceImpl - Implementation of Customer Domain Service
 * 
 * CHỨC NĂNG:
 * - Implement business logic cho customer operations
 * - Quản lý customer lifecycle
 * - Publish domain events
 * 
 * EVENT DRIVEN FEATURES:
 * - CustomerCreatedEvent: Publish khi customer được tạo
 * - CustomerUpdatedEvent: Publish khi customer được cập nhật
 * - CustomerDeletedEvent: Publish khi customer bị xóa
 * 
 * TRANSACTIONAL BEHAVIOR:
 * - @Transactional để đảm bảo data consistency
 * - Event publishing sau khi transaction commit
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerDomainServiceImpl implements CustomerDomainService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
        log.info("Creating customer with id: {}", customer.getId().getValue());
        
        // Validate customer
        validateCustomerForCreation(customer);
        
        // Check if customer already exists
        if (customerRepository.existsById(customer.getId().getValue())) {
            throw new BookingDomainException("Customer already exists with id: " + customer.getId().getValue());
        }
        
        // Check if username already exists
        if (customerRepository.findByUsername(customer.getUsername()).isPresent()) {
            throw new BookingDomainException("Username already exists: " + customer.getUsername());
        }
        
        // Set timestamps
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        
        // Save customer
        Customer savedCustomer = customerRepository.save(customer);
        
        // TODO: Publish CustomerCreatedEvent
        // publishCustomerCreatedEvent(savedCustomer);
        
        log.info("Customer created successfully with id: {}", savedCustomer.getId().getValue());
        return savedCustomer;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findCustomerById(UUID customerId) {
        log.debug("Finding customer by id: {}", customerId);
        return customerRepository.findById(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findCustomerByUsername(String username) {
        log.debug("Finding customer by username: {}", username);
        return customerRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public Customer updateCustomer(Customer customer) {
        log.info("Updating customer with id: {}", customer.getId().getValue());
        
        // Validate customer exists
        if (!customerRepository.existsById(customer.getId().getValue())) {
            throw new BookingDomainException("Customer not found with id: " + customer.getId().getValue());
        }
        
        // Set updated timestamp
        customer.setUpdatedAt(LocalDateTime.now());
        
        // Update customer
        Customer updatedCustomer = customerRepository.update(customer);
        
        // TODO: Publish CustomerUpdatedEvent
        // publishCustomerUpdatedEvent(updatedCustomer);
        
        log.info("Customer updated successfully with id: {}", updatedCustomer.getId().getValue());
        return updatedCustomer;
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID customerId) {
        log.info("Deleting customer with id: {}", customerId);
        
        // Find customer
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new BookingDomainException("Customer not found with id: " + customerId));
        
        // Soft delete
        customer.delete();
        customerRepository.update(customer);
        
        // TODO: Publish CustomerDeletedEvent
        // publishCustomerDeletedEvent(customer);
        
        log.info("Customer deleted successfully with id: {}", customerId);
    }

    @Override
    @Transactional
    public Customer activateCustomer(UUID customerId) {
        log.info("Activating customer with id: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new BookingDomainException("Customer not found with id: " + customerId));
        
        customer.activate();
        Customer activatedCustomer = customerRepository.update(customer);
        
        log.info("Customer activated successfully with id: {}", customerId);
        return activatedCustomer;
    }

    @Override
    @Transactional
    public Customer deactivateCustomer(UUID customerId) {
        log.info("Deactivating customer with id: {}", customerId);
        
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new BookingDomainException("Customer not found with id: " + customerId));
        
        customer.deactivate();
        Customer deactivatedCustomer = customerRepository.update(customer);
        
        log.info("Customer deactivated successfully with id: {}", customerId);
        return deactivatedCustomer;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean customerExists(UUID customerId) {
        return customerRepository.existsById(customerId);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Validate customer for creation
     * 
     * @param customer Customer cần validate
     * @throws BookingDomainException nếu validation fail
     */
    private void validateCustomerForCreation(Customer customer) {
        if (customer == null) {
            throw new BookingDomainException("Customer cannot be null");
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
    }

    // TODO: Implement event publishing methods
    /*
    private void publishCustomerCreatedEvent(Customer customer) {
        CustomerCreatedEvent event = new CustomerCreatedEvent(customer, LocalDateTime.now());
        // eventPublisher.publish(event);
    }
    
    private void publishCustomerUpdatedEvent(Customer customer) {
        CustomerUpdatedEvent event = new CustomerUpdatedEvent(customer, LocalDateTime.now());
        // eventPublisher.publish(event);
    }
    
    private void publishCustomerDeletedEvent(Customer customer) {
        CustomerDeletedEvent event = new CustomerDeletedEvent(customer, LocalDateTime.now());
        // eventPublisher.publish(event);
    }
    */
} 