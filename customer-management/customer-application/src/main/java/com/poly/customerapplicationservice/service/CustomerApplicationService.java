package com.poly.customerapplicationservice.service;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.exception.BlankUserIdException;
import com.poly.customerapplicationservice.exception.UserExistException;
import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.Loyalty;
import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyRepository;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CustomerApplicationService implements CustomerUsecase{

    private final CustomerRepository customerRepository;
    private final LoyaltyRepository loyaltyRepository;

    public CustomerApplicationService(CustomerRepository customerRepo,
                                      LoyaltyRepository loyaltyRepo) {
        this.customerRepository = customerRepo;
        this.loyaltyRepository = loyaltyRepo;
    }

    @Override
    public CustomerDto initializeCustomerProfile(CreateCustomerCommand command) {

        validateUserId(command.getUserId());

        Customer newCustomer = Customer.builder()
                .customerId(CustomerId.generate())
                .userId(command.getUserId())
                .name(Name.from(command.getFirstName().trim(), command.getLastName().trim()))
                .address(Address.from(command.getAddress().getStreet(), command.getAddress().getWard(), command.getAddress().getDistrict(), command.getAddress().getCity()))
                .dateOfBirth(DateOfBirth.from(command.getDateOfBirth()))
                .accumulatedSpending(Money.ZERO)
                .level(Level.NONE)
                .behaviorData(BehaviorData.empty())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Customer savedCustomer = customerRepository.save(newCustomer);

        Loyalty newLoyalty = Loyalty.createNew(newCustomer.getId());

        Loyalty savedLoyalty = loyaltyRepository.save(newLoyalty);

        return CustomerDto.from(newCustomer);
    }

    @Override
    public List<CustomerDto> getCustomers() {
        return List.of();
    }

    @Override
    public CustomerDto getCustomerById(UUID userID) {
        return null;
    }

    private void validateUserId(UUID userId) {
        if (userId == null) {
            throw new BlankUserIdException();
        }

        if (customerRepository.existsByUserId(userId)) {
            throw new UserExistException(userId);
        }
    }


}