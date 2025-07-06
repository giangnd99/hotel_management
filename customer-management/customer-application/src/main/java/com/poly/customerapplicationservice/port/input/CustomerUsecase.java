package com.poly.customerapplicationservice.port.input;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;

public interface CustomerUsecase {
    CustomerDto initializeCustomerProfile(CreateCustomerCommand command);
}
