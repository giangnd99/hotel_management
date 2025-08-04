package com.poly.customerapplicationservice.service;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.command.RetrieveCustomerProfileCommand;
import com.poly.customerapplicationservice.command.UpdateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.dto.PageResult;
import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.LoyaltyPoint;
import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.customerdomain.model.exception.BlankUserIdException;
import com.poly.customerdomain.model.exception.CustomerNotFoundException;
import com.poly.customerdomain.model.exception.UserExistException;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyPointRepository;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CustomerApplicationService implements CustomerUsecase {

    private final CustomerRepository customerRepository;

    private final LoyaltyPointRepository loyaltyPointRepository;

    public CustomerApplicationService(CustomerRepository customerRepo, LoyaltyPointRepository loyaltyRepo) {
        this.customerRepository = customerRepo;
        this.loyaltyPointRepository = loyaltyRepo;
    }

    @Override
    public CustomerDto initializeCustomerProfile(CreateCustomerCommand command) {

        validateUserId(command.getUserId(), Mode.CREATE);

        Customer newCustomer = Customer.builder()
                .customerId(CustomerId.generate())
                .userId(UserId.from(command.getUserId()))
                .name(Name.from(command.getFirstName().trim(), command.getLastName().trim()))
                .address(Address.from(command.getAddress().getStreet(), command.getAddress().getWard(), command.getAddress().getDistrict(), command.getAddress().getCity()))
                .dateOfBirth(DateOfBirth.from(command.getDateOfBirth()))
                .image(ImageUrl.empty())
                .level(Level.NONE)
                .behaviorData(BehaviorData.empty())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Customer savedCustomer = customerRepository.save(newCustomer);

        LoyaltyPoint newLoyaltyPoint = LoyaltyPoint.createNew(newCustomer.getId());

        LoyaltyPoint savedLoyaltyPoint = loyaltyPointRepository.save(newLoyaltyPoint);

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

        Customer customer = customerRepository.findByUserId(command.getUserId())
                .orElseThrow(() -> new CustomerNotFoundException(command.getUserId()));
        customer.setUserId(UserId.from(command.getUserId()));
        customer.setFullName(Name.from(command.getFirstName().trim(), command.getLastName().trim()));
        customer.setAddress(Address.from(command.getAddress().getStreet(), command.getAddress().getWard(), command.getAddress().getDistrict(), command.getAddress().getCity()));
        customer.setDateOfBirth(DateOfBirth.from(command.getDateOfBirth()));
        customer.setImage(ImageUrl.from(command.getImage()));
        customer.setUpdatedAt(LocalDateTime.now());

        Customer savedCustomer = customerRepository.save(customer);
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