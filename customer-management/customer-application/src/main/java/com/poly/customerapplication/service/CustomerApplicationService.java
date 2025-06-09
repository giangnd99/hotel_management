package com.poly.customerapplication.service;

import com.poly.customerapplication.port.CustomerUsecase;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.domain.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;

public class CustomerApplicationService implements CustomerUsecase {

    private CustomerRepository customerRepository;

    public CustomerApplicationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> getCustomerById(CustomerId id) {
        return Optional.empty();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return List.of();
    }
}
