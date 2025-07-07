package com.poly.customerapplicationservice.service;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.command.RetrieveCustomerProfileCommand;
import com.poly.customerapplicationservice.command.UpdateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.dto.PageResult;
import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.Loyalty;
import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.customerdomain.model.exception.BlankUserIdException;
import com.poly.customerdomain.model.exception.CustomerNotFoundException;
import com.poly.customerdomain.model.exception.UserExistException;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyRepository;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CustomerApplicationService implements CustomerUsecase{

    private final CustomerRepository customerRepository;

    private final LoyaltyRepository loyaltyRepository;

    public CustomerApplicationService(CustomerRepository customerRepo, LoyaltyRepository loyaltyRepo) {
        this.customerRepository = customerRepo;
        this.loyaltyRepository = loyaltyRepo;
    }

    @Override
    public CustomerDto initializeCustomerProfile(CreateCustomerCommand command) {

        validateUserId(command.getUserId(), Mode.CREATE);

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
    public CustomerDto retrieveCustomerProfile(RetrieveCustomerProfileCommand command) {
        validateUserId(command.getUserId(), Mode.RETRIEVE);

        return customerRepository.findByUserId(command.getUserId())
                .map(CustomerDto::from)
                .orElseThrow(() -> new CustomerNotFoundException(command.getUserId()));
    }

    @Override
    public PageResult<CustomerDto> retrieveAllCustomers(int page, int size) {
        List<Customer> allCustomers = customerRepository.findAll();
        int totalItems = allCustomers.size();
        int fromIndex = Math.min(page * size, totalItems);
        int toIndex = Math.min(fromIndex + size, totalItems);

        List<CustomerDto> pageItems = allCustomers.subList(fromIndex, toIndex)
                .stream()
                .map(CustomerDto::from)
                .toList();

        return new PageResult<>(pageItems, page, size, totalItems);
    }

    @Override
    public CustomerDto ChangeCustomerInformation(UpdateCustomerCommand command) {
        validateUserId(command.getUserId(), Mode.RETRIEVE);

        Optional<Customer> customer = customerRepository.findByUserId(command.getUserId());
        customer.ifPresent(c -> {
            c.setUserId(command.getUserId());
            c.setFullName(Name.from(command.getFirstName().trim(), command.getLastName().trim()));
            c.setAddress(Address.from(command.getAddress().getStreet(), command.getAddress().getWard(), command.getAddress().getDistrict(), command.getAddress().getCity()));
            c.setDateOfBirth(DateOfBirth.from(command.getDateOfBirth()));
            c.setUpdatedAt(LocalDateTime.now());
        });
        Customer savedCustomer = customerRepository.save(customer.get());
        return CustomerDto.from(savedCustomer);
    }

    private void validateUserId(UUID userId, Mode mode) {
        if (userId == null) {
            throw new BlankUserIdException();
        }

        if (mode == Mode.CREATE && customerRepository.existsByUserId(userId)) {
            throw new UserExistException(userId);
        }
    }

    private enum Mode {
        CREATE, RETRIEVE
    }
}