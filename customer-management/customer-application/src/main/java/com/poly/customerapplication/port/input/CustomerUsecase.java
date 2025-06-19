package com.poly.customerapplication.port.input;

import com.poly.customerapplication.dto.UpdateCustomerCommand;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.domain.valueobject.CustomerId;

import java.util.List;
import java.util.Optional;

public interface CustomerUsecase {

    Customer updateCustomer(UpdateCustomerCommand updateCustomerCommand);
    Optional<Customer> getCustomer(CustomerId customerId);
    Optional<Customer> getCustomerById(CustomerId id);
    List<Customer> getAllCustomers();
}
