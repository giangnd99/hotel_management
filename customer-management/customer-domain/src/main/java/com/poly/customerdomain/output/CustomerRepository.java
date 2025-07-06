package com.poly.customerdomain.output;

import com.poly.customerdomain.model.entity.Customer;
import com.poly.domain.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Customer save(Customer customer);                     // create or update
    Optional<Customer> findById(CustomerId customerId);   // optional: avoid null
    List<Customer> findAll();                             // list
    void delete(CustomerId customerId);                   // delete
    boolean existsByUserId(UUID userId);
}
