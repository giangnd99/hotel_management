package com.poly.restaurant.feign;

import com.poly.restaurant.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "customer-service", url = "${customer.service.url}")
public interface CustomerFeignClient {

    @GetMapping("/api/customers/{customerId}")
    CustomerDTO getCustomerById(@PathVariable String customerId);

    @GetMapping("/api/customers/{customerId}/room")
    CustomerDTO getCustomerRoomInfo(@PathVariable String customerId);
}
