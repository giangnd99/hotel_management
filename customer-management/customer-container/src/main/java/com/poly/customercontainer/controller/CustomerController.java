package com.poly.customercontainer.controller;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customercontainer.shared.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerUsecase customerUsecase;

    public CustomerController(CustomerUsecase customerUsecase) {
        this.customerUsecase = customerUsecase;
    }

//    @GetMapping()
//    public ResponseEntity<List<Customer>> retrieveAllCustomer() {
//
//    }

//    @GetMapping("/find")
//    public ResponseEntity<Customer> findById(CustomerId id) {
//
//    }

    // Tao thang tai khoan luon cho khach hang khi booking:
    @PostMapping("/initialize")
    public ResponseEntity<ApiResponse<CustomerDto>> initializeCustomer(@RequestBody CreateCustomerCommand createCustomerCommand) {
         var customerId = customerUsecase.initializeCustomerProfile(createCustomerCommand);
        return ResponseEntity.ok(ApiResponse.success(customerId));
    }
}
