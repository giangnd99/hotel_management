package com.poly.customercontainer.controller;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.command.RetrieveCustomerProfileCommand;
import com.poly.customerapplicationservice.command.UpdateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.dto.PageResult;
import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customercontainer.shared.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerUsecase customerUsecase;

    public CustomerController(CustomerUsecase customerUsecase) {
        this.customerUsecase = customerUsecase;
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<CustomerDto>> retrieveCustomerProfile(@PathVariable UUID userId) {
        RetrieveCustomerProfileCommand command = new RetrieveCustomerProfileCommand();
        command.setUserId(userId);
        var dto = customerUsecase.retrieveCustomerProfile(command);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<PageResult<CustomerDto>>> retrieveAllCustomers(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(ApiResponse.success(customerUsecase.retrieveAllCustomers(page, size)));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(@RequestBody CreateCustomerCommand createCustomerCommand) {
         var customerId = customerUsecase.initializeCustomerProfile(createCustomerCommand);
        return ResponseEntity.ok(ApiResponse.success(customerId));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(@RequestBody UpdateCustomerCommand updateCustomerCommand) {
        var customer = customerUsecase.ChangeCustomerInformation(updateCustomerCommand);
        return ResponseEntity.ok(ApiResponse.success(customer));
    }

}
