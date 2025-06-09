package com.poly.customerdataaccess.port;

import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.domain.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public Optional<Customer> findById(CustomerId id) {
        return Optional.empty();
    }

    @Override
    public List<Customer> findAll() {
        return List.of();
    }
}
