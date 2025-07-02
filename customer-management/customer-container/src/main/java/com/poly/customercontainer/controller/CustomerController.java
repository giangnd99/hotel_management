package com.poly.customercontainer.controller;

import com.poly.customerapplication.service.CustomerApplicationService;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.valueobject.*;
import com.poly.domain.valueobject.CustomerId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomerApplicationService  customerApplicationService;

    @GetMapping()
    public ResponseEntity<List<Customer>> retrieveAllCustomer() {

        Customer customer1 =  Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Hải Thạch"))
                .address(new Address("12 Lê Lợi", "Phường 4", "Quận 10", "TP.HCM"))
                .dateOfBirth(new DateOfBirth(LocalDate.of(1980, 1, 1)))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.regular())
                .build();

        Customer customer2 = Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Đằng Giang"))
                .address(new Address("12 Lê Lợi", "Phường 4", "Quận 10", "TP.HCM"))
                .dateOfBirth(new DateOfBirth(LocalDate.of(1980, 1, 1)))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.regular())
                .build();

        Customer customer3 =  Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Lâm Hùng"))
                .address(new Address("12 Lê Lợi", "Phường 4", "Quận 10", "TP.HCM"))
                .dateOfBirth(new DateOfBirth(LocalDate.of(1980, 1, 1)))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.regular())
                .build();

        Customer customer4 = Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Vũ Lâm"))
                .address(new Address("12 Lê Lợi", "Phường 4", "Quận 10", "TP.HCM"))
                .dateOfBirth(new DateOfBirth(LocalDate.of(1980, 1, 1)))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.regular())
                .build();

        Customer customer5=  Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Trí Tài"))
                .address(new Address("12 Lê Lợi", "Phường 4", "Quận 10", "TP.HCM"))
                .dateOfBirth(new DateOfBirth(LocalDate.of(1980, 1, 1)))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.regular())
                .build();

        Customer customer6 = Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Hoàng Linh"))
                .address(new Address("12 Lê Lợi", "Phường 4", "Quận 10", "TP.HCM"))
                .dateOfBirth(null)
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.regular())
                .build();

        List<Customer> list = List.of(
                customer1,
                customer2,
                customer3,
                customer4,
                customer5,
                customer6

        );
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/find")
    public ResponseEntity<Customer> findById(CustomerId id) {
        Customer customer = Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("John Doe"))
                .address(new Address("12 Lê Lợi", "Phường 4", "Quận 10", "TP.HCM"))
                .dateOfBirth(new DateOfBirth(LocalDate.of(1980, 1, 1)))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.regular())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
