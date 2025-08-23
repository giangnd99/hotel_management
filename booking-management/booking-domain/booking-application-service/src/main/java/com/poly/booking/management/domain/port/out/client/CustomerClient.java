package com.poly.booking.management.domain.port.out.client;

import com.poly.booking.management.domain.dto.request.CreateCustomerCommand;
import com.poly.booking.management.domain.dto.response.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.UUID;

@FeignClient(name = "customer-service", url = "localhost:8099/customers")
public interface CustomerClient {

    @GetMapping(value = "/{customerId}")
    CustomerDto getCustomerById(@PathVariable UUID customerId);

    @PostMapping
    CustomerDto createCustomer(@RequestBody CreateCustomerCommand customerDto);
}
