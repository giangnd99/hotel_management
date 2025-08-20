package com.poly.customerapplicationservice.port.input;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.command.RetrieveCustomerProfileCommand;
import com.poly.customerapplicationservice.command.UpdateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.dto.PageResult;

public interface CustomerUsecase {
    CustomerDto initializeCustomerProfile(CreateCustomerCommand command);
    CustomerDto retrieveCustomerProfile(RetrieveCustomerProfileCommand command);
    CustomerDto retrieveCustomerProfileById(RetrieveCustomerProfileCommand command);
    PageResult<CustomerDto> retrieveAllCustomers(int page, int size);
    CustomerDto ChangeCustomerInformation (UpdateCustomerCommand command);
}
