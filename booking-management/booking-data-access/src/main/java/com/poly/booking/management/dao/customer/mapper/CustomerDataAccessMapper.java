package com.poly.booking.management.dao.customer.mapper;

import com.poly.booking.management.dao.customer.entity.CustomerEntity;
import com.poly.booking.management.domain.entity.Customer;
import com.poly.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return Customer.Builder.builder()
                .id(new CustomerId(customerEntity.getId()))
                .email(customerEntity.getEmail())
                .username(customerEntity.getUsername())
                .name(customerEntity.getFirstName() + " " + customerEntity.getLastName())
                .build();
    }

    public CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .email(customer.getEmail())
                .username(customer.getUsername())
                .firstName(customer.getName().split(" ")[0])
                .lastName(customer.getName().split(" ")[1])
                .build();
    }
}
