package com.poly.booking.management.dao.customer.adapter;

import com.poly.booking.management.dao.customer.entity.CustomerEntity;
import com.poly.booking.management.dao.customer.mapper.CustomerDataAccessMapper;
import com.poly.booking.management.dao.customer.repository.CustomerJpaRepository;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.booking.management.domain.port.out.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    @Override
    public Customer save(Customer customer) {
        return customerDataAccessMapper.customerEntityToCustomer(customerJpaRepository
                .save(customerDataAccessMapper.customerToCustomerEntity(customer)));
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        CustomerEntity
        return customerJpaRepository.findById(customerId)
                .map(customerDataAccessMapper::customerEntityToCustomer);
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return customerJpaRepository.findByUsername(username)
                .map(customerDataAccessMapper::customerEntityToCustomer);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerJpaRepository.findByEmail(email)
                .map(customerDataAccessMapper::customerEntityToCustomer);
    }

    @Override
    public Customer update(Customer customer) {
        return customerDataAccessMapper.customerEntityToCustomer(customerJpaRepository.save(
                customerDataAccessMapper.customerToCustomerEntity(customer)));
    }

    @Override
    public void deleteById(UUID customerId) {
        customerJpaRepository.deleteById(customerId);
    }

    @Override
    public boolean existsById(UUID customerId) {
        return customerJpaRepository.existsById(customerId);
    }
}
