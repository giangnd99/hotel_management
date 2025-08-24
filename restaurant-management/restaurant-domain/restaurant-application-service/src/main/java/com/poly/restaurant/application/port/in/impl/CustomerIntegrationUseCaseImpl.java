package com.poly.restaurant.application.port.in.impl;

import com.poly.restaurant.application.dto.CustomerDTO;
import com.poly.restaurant.application.port.in.CustomerIntegrationUseCase;
import com.poly.restaurant.application.port.out.CustomerManagementPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerIntegrationUseCaseImpl implements CustomerIntegrationUseCase {

    private final CustomerManagementPort customerManagementPort;

    @Override
    public List<CustomerDTO> getAllCustomers(int page, int size) {
        log.info("Getting all customers with page={}, size={}", page, size);
        return customerManagementPort.getAllCustomers(page, size);
    }

    @Override
    public CustomerDTO getCustomerById(UUID customerId) {
        log.info("Getting customer by ID: {}", customerId);
        return customerManagementPort.getCustomerById(customerId);
    }

    @Override
    public CustomerDTO getCustomerProfileByUserId(UUID userId) {
        log.info("Getting customer profile by user ID: {}", userId);
        return customerManagementPort.getCustomerProfileByUserId(userId);
    }

    @Override
    public CustomerDTO getCustomerProfileById(UUID customerId) {
        log.info("Getting customer profile by customer ID: {}", customerId);
        return customerManagementPort.getCustomerProfileById(customerId);
    }

    @Override
    public boolean isCustomerValid(UUID customerId) {
        CustomerDTO customer = getCustomerById(customerId);
        return customer != null && customer.isActive();
    }

    @Override
    public String getCustomerFullName(UUID customerId) {
        CustomerDTO customer = getCustomerById(customerId);
        if (customer != null) {
            return customer.getFirstName() + " " + customer.getLastName();
        }
        return "Unknown Customer";
    }
}
