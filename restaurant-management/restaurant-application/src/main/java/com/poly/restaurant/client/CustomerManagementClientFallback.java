package com.poly.restaurant.client;

import com.poly.restaurant.dto.ApiResponseDTO;
import com.poly.restaurant.dto.CustomerDTO;
import com.poly.restaurant.dto.PageResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CustomerManagementClientFallback implements CustomerManagementClient {

    @Override
    public ApiResponseDTO<PageResultDTO<CustomerDTO>> getAllCustomers(int page, int size) {
        log.error("Customer management service is unavailable. Fallback triggered for getAllCustomers with page={}, size={}", page, size);
        return ApiResponseDTO.<PageResultDTO<CustomerDTO>>builder()
                .success(false)
                .message("Customer management service is temporarily unavailable")
                .build();
    }

    @Override
    public CustomerDTO getCustomerById(UUID customerId) {
        log.error("Customer management service is unavailable. Fallback triggered for getCustomerById with customerId={}", customerId);
        return null;
    }

    @Override
    public ApiResponseDTO<CustomerDTO> getCustomerProfileByUserId(UUID userId) {
        log.error("Customer management service is unavailable. Fallback triggered for getCustomerProfileByUserId with userId={}", userId);
        return ApiResponseDTO.<CustomerDTO>builder()
                .success(false)
                .message("Customer management service is temporarily unavailable")
                .build();
    }

    @Override
    public ApiResponseDTO<CustomerDTO> getCustomerProfileById(UUID customerId) {
        log.error("Customer management service is unavailable. Fallback triggered for getCustomerProfileById with customerId={}", customerId);
        return ApiResponseDTO.<CustomerDTO>builder()
                .success(false)
                .message("Customer management service is temporarily unavailable")
                .build();
    }
}
