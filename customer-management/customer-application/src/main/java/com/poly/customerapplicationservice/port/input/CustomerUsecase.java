package com.poly.customerapplicationservice.port.input;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;

import java.util.List;
import java.util.UUID;

public interface CustomerUsecase {
    CustomerDto initializeCustomerProfile(CreateCustomerCommand command);
    List<CustomerDto> getCustomers();
    CustomerDto getCustomerById(UUID userID);
//    CustomerDto updateCustomerProfile(UUID id, CreateCustomerCommand command);
}
