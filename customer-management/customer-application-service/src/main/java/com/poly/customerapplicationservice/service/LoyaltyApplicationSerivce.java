package com.poly.customerapplicationservice.service;

import com.poly.customerapplicationservice.command.RetriveLoyaltyProfileCommand;
import com.poly.customerapplicationservice.dto.LoyaltyDto;
import com.poly.customerapplicationservice.port.input.LoyaltyUsecase;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.Loyalty;
import com.poly.customerdomain.model.exception.BlankUserIdException;
import com.poly.customerdomain.model.exception.CustomerNotFoundException;
import com.poly.customerdomain.model.exception.LoyaltyNotFoundException;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyRepository;

public class LoyaltyApplicationSerivce implements LoyaltyUsecase {

    private final LoyaltyRepository loyaltyRepository;

    private final CustomerRepository customerRepository;

    public LoyaltyApplicationSerivce(LoyaltyRepository loyaltyRepository, CustomerRepository customerRepository) {
        this.loyaltyRepository = loyaltyRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public LoyaltyDto retriveLoyaltyProfile(RetriveLoyaltyProfileCommand command) {
        if (command.getCustomerId() == null) {
            throw new BlankUserIdException();
        }
        Customer customer = customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(command.getCustomerId()));
        Loyalty loyalty = loyaltyRepository.findByCustomerId(command.getCustomerId())
                .orElseThrow(() -> new LoyaltyNotFoundException(command.getCustomerId()));
        return LoyaltyDto.from(loyalty);
    }

}
