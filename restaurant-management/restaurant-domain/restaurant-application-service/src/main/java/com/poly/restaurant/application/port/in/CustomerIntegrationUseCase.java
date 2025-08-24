package com.poly.restaurant.application.port.in;

import com.poly.restaurant.application.dto.CustomerDTO;

import java.util.List;
import java.util.UUID;

/**
 * Use case interface for customer integration operations
 */
public interface CustomerIntegrationUseCase {

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

    /**
     * Validate if customer exists and is active
     */
    boolean isCustomerValid(UUID customerId);

    /**
     * Get customer full name
     */
    String getCustomerFullName(UUID customerId);
}
