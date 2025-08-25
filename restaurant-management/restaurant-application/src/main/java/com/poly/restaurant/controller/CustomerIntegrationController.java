//package com.poly.restaurant.controller;
//
//import com.poly.restaurant.application.dto.CustomerDTO;
//import com.poly.restaurant.application.port.in.CustomerIntegrationUseCase;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/restaurant/customers")
//@RequiredArgsConstructor
//@Tag(name = "Customer Integration Controller", description = "Integration with Customer Management Service")
//@Slf4j(topic = "RESTAURANT CUSTOMER INTEGRATION CONTROLLER")
//public class CustomerIntegrationController {
//
//    private final CustomerIntegrationUseCase customerIntegrationUseCase;
//
//    @GetMapping
//    @Operation(summary = "Get all customers with pagination")
//    public ResponseEntity<List<CustomerDTO>> getAllCustomers(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        log.info("Getting all customers with page={}, size={}", page, size);
//        List<CustomerDTO> customers = customerIntegrationUseCase.getAllCustomers(page, size);
//        return ResponseEntity.ok(customers);
//    }
//
//    @GetMapping("/{customerId}")
//    @Operation(summary = "Get customer by ID")
//    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID customerId) {
//        log.info("Getting customer by ID: {}", customerId);
//        CustomerDTO customer = customerIntegrationUseCase.getCustomerById(customerId);
//        if (customer != null) {
//            return ResponseEntity.ok(customer);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/profile/user/{userId}")
//    @Operation(summary = "Get customer profile by user ID")
//    public ResponseEntity<CustomerDTO> getCustomerProfileByUserId(@PathVariable UUID userId) {
//        log.info("Getting customer profile by user ID: {}", userId);
//        CustomerDTO customer = customerIntegrationUseCase.getCustomerProfileByUserId(userId);
//        if (customer != null) {
//            return ResponseEntity.ok(customer);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/profile/customer/{customerId}")
//    @Operation(summary = "Get customer profile by customer ID")
//    public ResponseEntity<CustomerDTO> getCustomerProfileById(@PathVariable UUID customerId) {
//        log.info("Getting customer profile by customer ID: {}", customerId);
//        CustomerDTO customer = customerIntegrationUseCase.getCustomerProfileById(customerId);
//        if (customer != null) {
//            return ResponseEntity.ok(customer);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @GetMapping("/{customerId}/validate")
//    @Operation(summary = "Validate if customer exists and is active")
//    public ResponseEntity<Boolean> validateCustomer(@PathVariable UUID customerId) {
//        log.info("Validating customer: {}", customerId);
//        boolean isValid = customerIntegrationUseCase.isCustomerValid(customerId);
//        return ResponseEntity.ok(isValid);
//    }
//
//    @GetMapping("/{customerId}/name")
//    @Operation(summary = "Get customer full name")
//    public ResponseEntity<String> getCustomerFullName(@PathVariable UUID customerId) {
//        log.info("Getting customer full name for: {}", customerId);
//        String fullName = customerIntegrationUseCase.getCustomerFullName(customerId);
//        return ResponseEntity.ok(fullName);
//    }
//}
