package com.poly.customerapplication.service;

import com.poly.customerapplication.dto.UpdateCustomerCommand;
import com.poly.customerapplication.port.input.CustomerUsecase;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.exception.CustomerDomainException;
import com.poly.customerdomain.model.exception.ErrorDomainCode;
import com.poly.customerdomain.model.valueobject.Address;
import com.poly.customerdomain.model.valueobject.Name;
import com.poly.customerdomain.model.valueobject.Nationality;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.domain.valueobject.CustomerId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CustomerApplicationService implements CustomerUsecase {

    private final CustomerRepository customerRepository;

    public CustomerApplicationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer updateCustomer(UpdateCustomerCommand command) {

        Customer customerToUpdate = customerRepository.findById(command.getId())
                .orElseThrow(() -> new CustomerDomainException(ErrorDomainCode.CUSTOMER_NOT_FOUND));

//        customerToUpdate.updateInfo(command.getName(), command.getAddress(), command.getDateOfBirth(), command.getNationality());

        return customerRepository.save(customerToUpdate);
    }

    @Override
    public Optional<Customer> getCustomer(CustomerId customerId) {
        return Optional.empty();
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
