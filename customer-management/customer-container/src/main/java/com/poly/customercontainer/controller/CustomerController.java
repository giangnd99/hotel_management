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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private CustomerApplicationService  customerApplicationService;

    @GetMapping()
    public ResponseEntity<List<Customer>> retrieveAllCustomer() {

        Customer customer1 =  Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Hải Thạch"))
                .address(new Address("123 Ấp 17, Xã Trung Chánh, Huyện Hóc Môn, TP. Hồ Chí Minh"))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.REGULAR)
                .build();

        Customer customer2 = Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Đằng Giang"))
                .address(new Address("123 Bình Dương, Tp. Hồ Chí Minh"))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.REGULAR)
                .build();

        Customer customer3 =  Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Lâm Hùng"))
                .address(new Address("123 Quận 7, TP. Hồ Chí Minh"))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.REGULAR)
                .build();

        Customer customer4 = Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Vũ Lâm"))
                .address(new Address("123 Quận 9, TP. Hồ Chí Minh"))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.REGULAR)
                .build();

        Customer customer5=  Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Trí Tài"))
                .address(new Address("123 Quận 12, TP. Hồ Chí Minh"))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.REGULAR)
                .build();

        Customer customer6 = Customer.builder()
                .userId(UUID.randomUUID())
                .name(new Name("Hoàng Linh"))
                .address(new Address("123 Quận Gò Vấp, TP. Hồ Chí Minh"))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.REGULAR)
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
                .address(new Address("123 Street, City, Country"))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .nationality(new Nationality("VIETNAME"))
                .customerType(CustomerType.REGULAR)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
