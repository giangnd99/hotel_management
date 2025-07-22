package com.poly.customerapplicationservice.service;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.command.RetrieveCustomerProfileCommand;
import com.poly.customerapplicationservice.command.UpdateCustomerCommand;
import com.poly.customerapplicationservice.dto.CustomerDto;
import com.poly.customerapplicationservice.dto.PageResult;
import com.poly.customerapplicationservice.shared.AddressDto;
import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.customerdomain.model.exception.BlankUserIdException;
import com.poly.customerdomain.model.exception.CustomerNotFoundException;
import com.poly.customerdomain.model.exception.UserExistException;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyPointRepository;
import com.poly.domain.valueobject.CustomerId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerApplicationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LoyaltyPointRepository loyaltyPointRepository;

    @InjectMocks
    private CustomerApplicationService service;

    private final UUID userId = UUID.randomUUID();

    private final CustomerId customerId = CustomerId.generate();

    @Test
    void shouldInitializeCustomerProfileSuccessfully() {
        CreateCustomerCommand command = new CreateCustomerCommand();
        command.setUserId(userId);
        command.setFirstName("Nguyen");
        command.setLastName("Van A");
        command.setDateOfBirth(LocalDate.of(2000, 1, 1));
        command.setAddress(new AddressDto("123 Street", "Ward 123", "District 123", "City 123"));
        when(customerRepository.existsByUserId(userId)).thenReturn(false);
        when(customerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(loyaltyPointRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CustomerDto result = service.initializeCustomerProfile(command);
        assertNotNull(result);
        assertNotNull(result.getCustomerId());
        assertEquals("Nguyen Van A", result.getFirstName() +  " " + result.getLastName());
        assertEquals("123 Street, Ward 123, District 123, City 123", result.getAddress());
    }

    @Test
    void shouldThrowBlankUserIdExceptionWhenUserIdIsNullOnCreate() {
        CreateCustomerCommand command = new CreateCustomerCommand();
        command.setUserId(null);
        assertThrows(BlankUserIdException.class, () -> service.initializeCustomerProfile(command));
    }

    @Test
    void shouldThrowUserExistExceptionWhenUserIdAlreadyExists() {
        CreateCustomerCommand command = new CreateCustomerCommand();
        command.setUserId(userId);
        command.setFirstName("Test");
        command.setLastName("User");
        command.setDateOfBirth(LocalDate.of(2000, 1, 1));
        command.setAddress(new AddressDto("Street", "Ward", "District", "City"));
        when(customerRepository.existsByUserId(userId)).thenReturn(true);
        assertThrows(UserExistException.class, () -> service.initializeCustomerProfile(command));
    }

    //retrieveCustomerProfile
    @Test
    void shouldRetrieveCustomerProfileSuccessfully() {
        RetrieveCustomerProfileCommand command = new RetrieveCustomerProfileCommand();
        command.setUserId(userId);

        Customer customer = Customer.builder()
                .customerId(customerId)
                .userId(UserId.from(userId))
                .name(Name.from("ABC", "ABC"))
                .image(ImageUrl.empty())
                .address(Address.from("123", "Ward", "District", "City"))
                .dateOfBirth(DateOfBirth.from(LocalDate.of(2000, 1, 1)))
                .build();
        when(customerRepository.findByUserId(userId)).thenReturn(Optional.of(customer));
        CustomerDto result = service.retrieveCustomerProfile(command);
        assertNotNull(result);
        assertNotNull(result.getCustomerId());
        assertEquals("ABC ABC", result.getFirstName() +  " " + result.getLastName());
        assertEquals("123, Ward, District, City", result.getAddress());
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenCustomerNotFound() {
        RetrieveCustomerProfileCommand command = new RetrieveCustomerProfileCommand();
        command.setUserId(userId);
        when(customerRepository.findByUserId(userId)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> service.retrieveCustomerProfile(command));
    }

    //retrieveAllCustomers
    @Test
    void shouldRetrieveAllCustomersWithPagination() {
        List<Customer> customers = List.of(
                Customer.builder().customerId(customerId).userId(UserId.from(UUID.randomUUID())).build(),
                Customer.builder().customerId(CustomerId.generate()).userId(UserId.from(UUID.randomUUID())).build()
        );
        when(customerRepository.findAll()).thenReturn(customers);
        PageResult<CustomerDto> result = service.retrieveAllCustomers(0, 1);
        assertEquals(1, result.getItems().size());
        assertEquals(2, result.getTotalItems());
    }

    //ChangeCustomerInformation
    @Test
    void shouldUpdateCustomerInformationSuccessfully() {
        UpdateCustomerCommand command = new UpdateCustomerCommand();
        command.setUserId(userId);
        command.setFirstName("Updated");
        command.setLastName("Name");
        command.setDateOfBirth(LocalDate.of(2000, 1, 1));
        command.setImage("https://img.jpg");
        command.setAddress(new AddressDto("123", "Ward", "District", "City"));
        Customer customer = Customer.builder().customerId(customerId).userId(UserId.from(userId)).build();
        when(customerRepository.findByUserId(userId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);
        CustomerDto result = service.ChangeCustomerInformation(command);
        assertNotNull(result);
    }

    @Test
    void shouldThrowBlankUserIdExceptionWhenUserIdIsNullOnUpdate() {
        UpdateCustomerCommand command = new UpdateCustomerCommand();
        command.setUserId(null);
        assertThrows(BlankUserIdException.class, () -> service.ChangeCustomerInformation(command));
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenUpdatingNonExistentCustomer() {
        UpdateCustomerCommand command = new UpdateCustomerCommand();
        command.setUserId(userId);
        when(customerRepository.findByUserId(userId)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> service.ChangeCustomerInformation(command));
    }


}
