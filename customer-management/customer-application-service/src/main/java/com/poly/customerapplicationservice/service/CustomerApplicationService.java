package com.poly.customerapplicationservice.service;

import com.poly.customerapplicationservice.dto.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.dto.command.RetrieveCustomerProfileCommand;
import com.poly.customerapplicationservice.dto.command.UpdateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.dto.PageResult;
import com.poly.customerapplicationservice.dto.request.UserCreationRequest;
import com.poly.customerapplicationservice.dto.response.UserResponse;
import com.poly.customerapplicationservice.message.CustomerBookingMessage;
import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customerapplicationservice.port.output.feign.AuthenticationClient;
import com.poly.customerapplicationservice.port.output.feign.NotificationClient;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class CustomerApplicationService implements CustomerUsecase {

    private final CustomerRepository customerRepository;

    private final LoyaltyPointRepository loyaltyPointRepository;

    private final CustomerCreationRequestPublisher customerCreationRequestPublisher;

    private final AuthenticationClient authenticationClient;

    private final CustomerAsyncService customerAsyncService;

    public CustomerApplicationService(CustomerRepository customerRepo, LoyaltyPointRepository loyaltyRepo, CustomerCreationRequestPublisher customerCreationRequestPublisher, AuthenticationClient authenticationClient, CustomerAsyncService customerAsyncService) {
        this.customerRepository = customerRepo;
        this.loyaltyPointRepository = loyaltyRepo;
        this.customerCreationRequestPublisher = customerCreationRequestPublisher;
        this.authenticationClient = authenticationClient;
        this.customerAsyncService = customerAsyncService;
    }

    @Override
    @Transactional
    public CustomerDto initializeCustomerProfile(CreateCustomerCommand command) {

        UserCreationRequest creationRequest = creationRequest(command);
        UserResponse response = authenticationClient.createUser(creationRequest).getResult();

        Address address = command.getAddress() == null ? Address.empty() : Address.from(command.getAddress().getStreet(), command.getAddress().getWard(), command.getAddress().getDistrict(), command.getAddress().getCity());
        DateOfBirth dateOfBirth = command.getDateOfBirth() == null ? DateOfBirth.empty() : DateOfBirth.from(command.getDateOfBirth());
        Name name = command.getFirstName() == null || command.getLastName() == null ? Name.empty() : Name.from(command.getFirstName().trim(), command.getLastName().trim());

        Customer newCustomer = Customer.builder()
                .customerId(CustomerId.generate())
                .userId(UserId.from(UUID.fromString(response.getId())))
                .name(name)
                .address(address)
                .dateOfBirth(dateOfBirth)
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

        log.info("User id created successfully: {} ", response.getId());
        log.info("Customer with id: {} created successfully", savedCustomer.getId().getValue().toString());
        customerCreationRequestPublisher.publish(creatMessage(savedCustomer, response));
        customerAsyncService.sendNotifications(creationRequest);
        return CustomerDto.from(savedCustomer);
    }

    private CustomerBookingMessage creatMessage(Customer customer, UserResponse response) {
        return CustomerBookingMessage.builder()
                .customerId(customer.getId().getValue().toString())
                .firstName(customer.getFullName().getFirstName())
                .lastName(customer.getFullName().getLastName())
                .username(response.getEmail())
                .active(customer.isActive())
                .build();
    }
    private UserCreationRequest creationRequest(CreateCustomerCommand command) {
        String email = command.getEmail() == null ? "needToChange@gmail.com" : command.getEmail().trim();
        String phone = command.getPhone() == null ? "0123456789" : command.getPhone().trim();
        String password = command.getPassword() == null ? "admin123" : command.getPassword().trim();
        return UserCreationRequest.builder()
                .email(email)
                .password(password)
                .phone(phone)
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
        validateUserId(command.getCustomerId(), Mode.RETRIEVE);

        Customer customer = customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(command.getCustomerId()));
//        customer.setUserId(UserId.from(command.getCustomerId() != null ?  command.getCustomerId() : null));
        customer.setFullName(Name.from(command.getFirstName().trim(), command.getLastName().trim()));
        customer.setAddress(Address.from(command.getAddress().getStreet(), command.getAddress().getWard(), command.getAddress().getDistrict(), command.getAddress().getCity()));
        customer.setDateOfBirth(DateOfBirth.from(command.getDateOfBirth()));
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setSex(Sex.valueOf(command.getSex().toUpperCase()));
        customer.setActive(command.isActive());

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

    public CustomerDto updateCustomerAvatar(String customerId, String imageLink) {
        UUID customerIdFinded = UUID.fromString(customerId);

        validateUserId(customerIdFinded, Mode.RETRIEVE);
        Customer customer = customerRepository.findById(customerIdFinded)
                .orElseThrow(() -> new CustomerNotFoundException(customerIdFinded));

        customer.setImage(ImageUrl.from(imageLink));
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerDto.from(savedCustomer);

    }

    private void validateUserId(UUID userId, Mode mode) {
        if (userId == null) {
            throw new BlankUserIdException();
        }

        if (mode == Mode.CREATE && customerRepository.findById(userId).isPresent()) {
            throw new UserExistException(userId);
        }
    }

    private enum Mode {
        CREATE, RETRIEVE
    }
}