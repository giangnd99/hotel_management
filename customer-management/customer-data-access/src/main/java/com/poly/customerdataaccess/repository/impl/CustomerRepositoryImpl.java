package com.poly.customerdataaccess.repository.impl;

import com.poly.customerdataaccess.entity.CustomerEntity;
import com.poly.customerdataaccess.repository.CustomerJpaRepository;
import com.poly.customerdataaccess.mapper.CustomerDataMapper;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.domain.valueobject.CustomerId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    public CustomerRepositoryImpl(CustomerJpaRepository jpa) {
        this.customerJpaRepository = jpa;
    }

    @Override
    public Customer save(Customer customer) {
        var entity = CustomerDataMapper.mapToEntity(customer);
        var saved = customerJpaRepository.save(entity);
        return CustomerDataMapper.mapToDomain(saved);
    }

    @Override
    public Optional<Customer> findByUserId(UUID userId) {
        return customerJpaRepository.findByUserId(userId)
                .map(CustomerDataMapper::mapToDomain);
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        return customerJpaRepository.findById(customerId).map(CustomerDataMapper::mapToDomain);
    }


    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll()
                .stream()
                .map(CustomerDataMapper::mapToDomain)
                .toList();
    }

    @Override
    public void delete(CustomerId customerId) {
        customerJpaRepository.deleteById(Integer.parseInt(customerId.getValue().toString()));
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        Optional<CustomerEntity> customer = customerJpaRepository.findByUserId(userId);
        if (customer.isPresent()) {
            return true;
        }
        return false;
    }

}
