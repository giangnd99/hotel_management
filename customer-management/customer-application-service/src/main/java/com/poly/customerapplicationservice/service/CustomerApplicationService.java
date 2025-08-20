package com.poly.customerapplicationservice.service;

import com.poly.customerapplicationservice.dto.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.dto.command.RetrieveCustomerProfileCommand;
import com.poly.customerapplicationservice.dto.command.UpdateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.dto.PageResult;
import com.poly.customerapplicationservice.message.CustomerBookingMessage;
import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customerapplicationservice.port.output.publisher.CustomerCreationRequestPublisher;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.LoyaltyPoint;
import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.customerdomain.model.exception.BlankUserIdException;
import com.poly.customerdomain.model.exception.CustomerNotFoundException;
import com.poly.customerdomain.model.exception.UserExistException;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyPointRepository;
import com.poly.domain.valueobject.CustomerId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class CustomerApplicationService implements CustomerUsecase {

    private final CustomerRepository customerRepository;

    private final LoyaltyPointRepository loyaltyPointRepository;

    private final CustomerCreationRequestPublisher customerCreationRequestPublisher;

    public CustomerApplicationService(CustomerRepository customerRepo, LoyaltyPointRepository loyaltyRepo, CustomerCreationRequestPublisher customerCreationRequestPublisher) {
        this.customerRepository = customerRepo;
        this.loyaltyPointRepository = loyaltyRepo;
        this.customerCreationRequestPublisher = customerCreationRequestPublisher;
    }

    @Override
    public CustomerDto initializeCustomerProfile(CreateCustomerCommand command) {

//        validateUserId(command.getUserId(), Mode.RETRIEVE);

        Customer newCustomer = Customer.builder()
                .customerId(CustomerId.generate())
                .userId(UserId.from(command.getUserId() != null ? command.getUserId() : null))
                .name(Name.from(command.getFirstName().trim(), command.getLastName().trim()))
                .address(Address.from(command.getAddress().getStreet(), command.getAddress().getWard(), command.getAddress().getDistrict(), command.getAddress().getCity()))
                .dateOfBirth(DateOfBirth.from(command.getDateOfBirth()))
                .image(ImageUrl.empty())
                .level(Level.NONE)
                .behaviorData(BehaviorData.empty())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .sex(Sex.valueOf(command.getSex().toUpperCase()))
                .active(true)
                .build();

        Customer savedCustomer = customerRepository.save(newCustomer);

        LoyaltyPoint newLoyaltyPoint = LoyaltyPoint.createNew(newCustomer.getId());

        LoyaltyPoint savedLoyaltyPoint = loyaltyPointRepository.save(newLoyaltyPoint);
        //send request to Booking service
        customerCreationRequestPublisher.publish(creatMessage(savedCustomer));

        return CustomerDto.from(newCustomer);
    }

    private CustomerBookingMessage creatMessage(Customer customer) {
        return CustomerBookingMessage.builder()
                .customerId(customer.getId().getValue().toString())
                .firstName(customer.getFullName().getFirstName())
                .lastName(customer.getFullName().getLastName())
                .username(customer.getUserId() == null ? "" : customer.getUserId().getValue().toString())
                .active(customer.isActive())
                .build();
    }

    @Override
    public CustomerDto retrieveCustomerProfile(RetrieveCustomerProfileCommand command) {
        validateUserId(command.getUserId(), Mode.RETRIEVE);

        return customerRepository.findByUserId(command.getUserId())
                .map(CustomerDto::from)
                .orElseThrow(() -> new CustomerNotFoundException(command.getUserId()));
    }

    @Override
    public CustomerDto retrieveCustomerProfileById(RetrieveCustomerProfileCommand command) {
        validateUserId(command.getUserId(), Mode.RETRIEVE);

        return customerRepository.findById(command.getUserId())
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

    @Override
    public CustomerDto findCustomerById(UUID customerId) {
        if (customerId == null) {
            log.error("Customer id is null");
            throw new RuntimeException("Customer id is null");
        }
        return customerRepository.findById(customerId).map(CustomerDto::from)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
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