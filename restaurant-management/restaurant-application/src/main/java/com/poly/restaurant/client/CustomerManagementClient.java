package com.poly.restaurant.client;

import com.poly.restaurant.dto.ApiResponseDTO;
import com.poly.restaurant.dto.CustomerDTO;
import com.poly.restaurant.dto.PageResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
    name = "customer-management",
    url = "${customer.service.url:http://localhost:8099}",
    fallback = CustomerManagementClientFallback.class
)
public interface CustomerManagementClient {

    @GetMapping("/customers")
    ApiResponseDTO<PageResultDTO<CustomerDTO>> getAllCustomers(
        @RequestParam("page") int page,
        @RequestParam("size") int size
    );

    @GetMapping("/customers/{customerId}")
    CustomerDTO getCustomerById(@PathVariable UUID customerId);

    @GetMapping("/customers/profile/{userId}")
    ApiResponseDTO<CustomerDTO> getCustomerProfileByUserId(@PathVariable UUID userId);

    @GetMapping("/customers/profile/customer/{customerId}")
    ApiResponseDTO<CustomerDTO> getCustomerProfileById(@PathVariable UUID customerId);
}
