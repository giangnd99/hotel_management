package com.poly.customercontainer.controller;

import com.poly.customerapplication.service.CustomerApplicationService;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.valueobject.Address;
import com.poly.customerdomain.model.valueobject.CustomerType;
import com.poly.customerdomain.model.valueobject.Name;
import com.poly.customerdomain.model.valueobject.Nationality;
import com.poly.domain.valueobject.CustomerId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private CustomerApplicationService  customerApplicationService;

    @GetMapping
    public ResponseEntity<Customer> findById(CustomerId id) {
        Customer customer = new Customer.Builder()
                .userId(UUID.randomUUID())
                .name(new Name("John Doe"))
                .address(new Address("123 Street, City, Country"))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.REGULAR)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
