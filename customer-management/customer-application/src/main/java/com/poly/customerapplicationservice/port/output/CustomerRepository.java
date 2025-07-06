package com.poly.customerapplicationservice.port.output;

import com.poly.customerdomain.model.entity.Customer;
import com.poly.domain.valueobject.CustomerId;

import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(CustomerId id);
}