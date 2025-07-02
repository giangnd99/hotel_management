package com.poly.customerdomain.output;

import com.poly.customerdomain.model.entity.Customer;
import com.poly.domain.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Customer save(Customer customer);
    Customer find(CustomerId customerId);
    Customer update(Customer customer);
    void delete(CustomerId customerId);
    Optional<Customer> findById(CustomerId id);
    List<Customer> findAll();
}
