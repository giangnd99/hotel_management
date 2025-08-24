package com.poly.restaurant.dataaccess.adapter;

import com.poly.restaurant.application.dto.CustomerDTO;
import com.poly.restaurant.application.port.out.CustomerManagementPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class CustomerManagementPortMock implements CustomerManagementPort {

    @Override
    public List<CustomerDTO> getAllCustomers(int page, int size) {
        log.info("Mock: Getting all customers with page={}, size={}", page, size);
        
        // Return mock customer data
        return List.of(
            createMockCustomer(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"),
                "Nguyen", "Van A", "123A Lê Lợi, Q1, TP.HCM",
                LocalDate.of(2000, 1, 1), BigDecimal.valueOf(1200.50),
                "BRONZE", true, "MALE"
            ),
            createMockCustomer(
                UUID.fromString("22222222-2222-2222-2222-222222222222"),
                UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"),
                "Tran", "Thi B", "456B Nguyễn Huệ, Q1, TP.HCM",
                LocalDate.of(1995, 5, 15), BigDecimal.valueOf(2500.75),
                "SILVER", true, "FEMALE"
            )
        );
    }

    @Override
    public CustomerDTO getCustomerById(UUID customerId) {
        log.info("Mock: Getting customer by ID: {}", customerId);
        
        // Return mock customer based on ID
        if ("11111111-1111-1111-1111-111111111111".equals(customerId.toString())) {
            return createMockCustomer(
                customerId,
                UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"),
                "Nguyen", "Van A", "123A Lê Lợi, Q1, TP.HCM",
                LocalDate.of(2000, 1, 1), BigDecimal.valueOf(1200.50),
                "BRONZE", true, "MALE"
            );
        } else if ("22222222-2222-2222-2222-222222222222".equals(customerId.toString())) {
            return createMockCustomer(
                customerId,
                UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12"),
                "Tran", "Thi B", "456B Nguyễn Huệ, Q1, TP.HCM",
                LocalDate.of(1995, 5, 15), BigDecimal.valueOf(2500.75),
                "SILVER", true, "FEMALE"
            );
        }
        
        return null; // Customer not found
    }

    @Override
    public CustomerDTO getCustomerProfileByUserId(UUID userId) {
        log.info("Mock: Getting customer profile by user ID: {}", userId);
        
        // For simplicity, return the same mock data as getCustomerById
        // In a real implementation, you would map userId to customerId
        if ("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11".equals(userId.toString())) {
            return getCustomerById(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        } else if ("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12".equals(userId.toString())) {
            return getCustomerById(UUID.fromString("22222222-2222-2222-2222-222222222222"));
        }
        
        return null; // User not found
    }

    @Override
    public CustomerDTO getCustomerProfileById(UUID customerId) {
        log.info("Mock: Getting customer profile by customer ID: {}", customerId);
        
        // Same as getCustomerById for simplicity
        return getCustomerById(customerId);
    }

    private CustomerDTO createMockCustomer(UUID customerId, UUID userId, String firstName, String lastName, 
                                         String address, LocalDate dateOfBirth, BigDecimal accumulatedSpending,
                                         String level, boolean active, String sex) {
        return CustomerDTO.builder()
                .customerId(customerId)
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .accumulatedSpending(accumulatedSpending)
                .level(level)
                .imageUrl(null)
                .createdDate(LocalDateTime.now().minusDays(30))
                .updatedDate(LocalDateTime.now())
                .sex(sex)
                .active(active)
                .build();
    }
}
