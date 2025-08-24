package com.poly.customercontainer.controller;

import com.poly.customerapplicationservice.dto.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.dto.command.RetrieveCustomerProfileCommand;
import com.poly.customerapplicationservice.dto.command.UpdateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.dto.PageResult;
import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customercontainer.shared.request.ApiResponse;
import com.poly.customerdataaccess.image.CloudinaryImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerUsecase customerUsecase;

    private final CloudinaryImage cloudinaryImage;

    public CustomerController(CustomerUsecase customerUsecase, CloudinaryImage cloudinaryImage) {
        this.customerUsecase = customerUsecase;
        this.cloudinaryImage = cloudinaryImage;
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<CustomerDto>> retrieveCustomerProfile(@PathVariable UUID userId) {
        RetrieveCustomerProfileCommand command = new RetrieveCustomerProfileCommand();
        command.setUserId(userId);
        var dto = customerUsecase.retrieveCustomerProfile(command);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/profile/customer/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDto>> retrieveCustomerProfileById(@PathVariable UUID customerId) {
        RetrieveCustomerProfileCommand command = new RetrieveCustomerProfileCommand();
        command.setUserId(customerId);
        var dto = customerUsecase.retrieveCustomerProfileById(command);
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

    @PutMapping(value = "/profile")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(@RequestBody UpdateCustomerCommand command) {
        var customer = customerUsecase.ChangeCustomerInformation(command);
        return ResponseEntity.ok(ApiResponse.success(customer));
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable UUID customerId) {
        try {
            CustomerDto response = customerUsecase.findCustomerById(customerId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Not found customer with id: {}", customerId);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/profile/{customerId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomerAvatar(
            @PathVariable String customerId,
            @RequestPart("imageRaw") MultipartFile imageFile) {

        if (imageFile == null || imageFile.isEmpty()) {
            throw new RuntimeException("Ảnh không hợp lệ");
        }

        try {
            byte[] imageBytes = imageFile.getBytes();
            String imageLink = cloudinaryImage.upload(imageBytes);

            var customer = customerUsecase.updateCustomerAvatar(customerId, imageLink);
            return ResponseEntity.ok(ApiResponse.success(customer));
        } catch (IOException e) {
            throw new RuntimeException("Không đọc được ảnh", e);
        }
    }

}
