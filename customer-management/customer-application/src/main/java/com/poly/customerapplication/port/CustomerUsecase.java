package com.poly.customerapplication.port;

import com.poly.customerdomain.model.entity.Customer;
import com.poly.domain.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;

public interface CustomerUsecase {
    Optional<Customer> getCustomerById(CustomerId id);
    List<Customer> getAllCustomers();
}
