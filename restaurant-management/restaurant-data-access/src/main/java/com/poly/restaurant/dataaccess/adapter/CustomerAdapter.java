package com.poly.restaurant.adapter;

import com.poly.restaurant.application.dto.CustomerDTO;
import com.poly.restaurant.application.port.out.CustomerManagementPort;
import com.poly.restaurant.client.CustomerFeignClient;
import com.poly.restaurant.dto.ApiResponseDTO;
import com.poly.restaurant.dto.PageResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerAdapter implements CustomerManagementPort {

    private final CustomerFeignClient customerManagementClient;

    @Override
    public List<CustomerDTO> getAllCustomers(int page, int size) {
        log.info("Fetching customers from customer management service with page={}, size={}", page, size);

        try {
            ApiResponseDTO<PageResultDTO<com.poly.restaurant.dto.CustomerDTO>> response = customerManagementClient.getAllCustomers(page, size);

            if (response != null && response.isSuccess() && response.getData() != null) {
                log.info("Successfully fetched {} customers from customer management service",
                    response.getData().getItems().size());
                return response.getData().getItems().stream()
                        .map(this::mapApplicationDtoToDomainDto)
                        .collect(Collectors.toList());
            } else {
                log.warn("Failed to fetch customers from customer management service. Response: {}", response);
                return List.of();
            }
        } catch (Exception e) {
            log.error("Error fetching customers from customer management service", e);
            return List.of();
        }
    }

    @Override
    public CustomerDTO getCustomerById(UUID customerId) {
        log.info("Fetching customer by ID: {}", customerId);

        try {
            com.poly.restaurant.dto.CustomerDTO customer = customerManagementClient.getCustomerById(customerId);
            if (customer != null) {
                log.info("Successfully fetched customer: {}", customer.getFirstName() + " " + customer.getLastName());
            } else {
                log.warn("Customer not found with ID: {}", customerId);
            }
            return mapApplicationDtoToDomainDto(customer);
        } catch (Exception e) {
            log.error("Error fetching customer by ID: {}", customerId, e);
            return null;
        }
    }

    @Override
    public CustomerDTO getCustomerProfileByUserId(UUID userId) {
        log.info("Fetching customer profile by user ID: {}", userId);

        try {
            ApiResponseDTO<com.poly.restaurant.dto.CustomerDTO> response = customerManagementClient.getCustomerProfileByUserId(userId);

            if (response != null && response.isSuccess() && response.getData() != null) {
                log.info("Successfully fetched customer profile for user ID: {}", userId);
                return mapApplicationDtoToDomainDto(response.getData());
            } else {
                log.warn("Failed to fetch customer profile for user ID: {}. Response: {}", userId, response);
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching customer profile by user ID: {}", userId, e);
            return null;
        }
    }

    @Override
    public CustomerDTO getCustomerProfileById(UUID customerId) {
        log.info("Fetching customer profile by customer ID: {}", customerId);

        try {
            ApiResponseDTO<com.poly.restaurant.dto.CustomerDTO> response = customerManagementClient.getCustomerProfileById(customerId);

            if (response != null && response.isSuccess() && response.getData() != null) {
                log.info("Successfully fetched customer profile for customer ID: {}", customerId);
                return mapApplicationDtoToDomainDto(response.getData());
            } else {
                log.warn("Failed to fetch customer profile for customer ID: {}. Response: {}", customerId, response);
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching customer profile by customer ID: {}", customerId, e);
            return null;
        }
    }

    // Helper method to map from application DTO to domain DTO
    private CustomerDTO mapApplicationDtoToDomainDto(com.poly.restaurant.dto.CustomerDTO applicationDto) {
        if (applicationDto == null) return null;
        return CustomerDTO.builder()
                .customerId(applicationDto.getCustomerId())
                .userId(applicationDto.getUserId())
                .firstName(applicationDto.getFirstName())
                .lastName(applicationDto.getLastName())
                .address(applicationDto.getAddress())
                .dateOfBirth(applicationDto.getDateOfBirth())
                .accumulatedSpending(applicationDto.getAccumulatedSpending())
                .level(applicationDto.getLevel())
                .imageUrl(applicationDto.getImageUrl())
                .createdDate(applicationDto.getCreatedDate())
                .updatedDate(applicationDto.getUpdatedDate())
                .sex(applicationDto.getSex())
                .active(applicationDto.isActive())
                .build();
    }
}
