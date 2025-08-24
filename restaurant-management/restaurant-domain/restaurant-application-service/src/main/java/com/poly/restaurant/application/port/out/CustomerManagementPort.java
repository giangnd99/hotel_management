package com.poly.restaurant.application.port.out;

import com.poly.restaurant.application.dto.CustomerDTO;

import java.util.List;
import java.util.UUID;

/**
 * Port interface for customer management operations
 * This is the abstract layer that the domain uses to communicate with external services
 */
public interface CustomerManagementPort {

    /**
     * Get all customers with pagination
     */
    List<CustomerDTO> getAllCustomers(int page, int size);

    /**
     * Get customer by ID
     */
    CustomerDTO getCustomerById(UUID customerId);

    /**
     * Get customer profile by user ID
     */
    CustomerDTO getCustomerProfileByUserId(UUID userId);

    /**
     * Get customer profile by customer ID
     */
    CustomerDTO getCustomerProfileById(UUID customerId);
}
