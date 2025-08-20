package com.poly.customerapplicationservice.port.input;

import com.poly.customerapplicationservice.dto.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.dto.command.RetrieveCustomerProfileCommand;
import com.poly.customerapplicationservice.dto.command.UpdateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.dto.PageResult;

import java.util.UUID;

public interface CustomerUsecase {
    CustomerDto initializeCustomerProfile(CreateCustomerCommand command);
    CustomerDto retrieveCustomerProfile(RetrieveCustomerProfileCommand command);
    CustomerDto retrieveCustomerProfileById(RetrieveCustomerProfileCommand command);
    PageResult<CustomerDto> retrieveAllCustomers(int page, int size);
    CustomerDto ChangeCustomerInformation (UpdateCustomerCommand command);
<<<<<<< HEAD
    CustomerDto findCustomerById(UUID customerId);
=======
    CustomerDto updateCustomerAvatar(String customerId, String imageLink);
>>>>>>> 9e5c2efb57e81c48ff4e55f850db5a31a866592d
}
