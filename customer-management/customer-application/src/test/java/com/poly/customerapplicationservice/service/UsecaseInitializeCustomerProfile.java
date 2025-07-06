package com.poly.customerapplicationservice.service;

import com.poly.customerdomain.model.entity.Customer;
import com.poly.customerdomain.model.entity.Loyalty;
import com.poly.customerdomain.output.CustomerRepository;
import com.poly.customerdomain.output.LoyaltyRepository;
import com.poly.customerdomain.output.VoucherRepository;
import com.poly.domain.valueobject.CustomerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UsecaseInitializeCustomerProfile {

    private CustomerRepository customerRepository;
    private LoyaltyRepository loyaltyRepository;
    private VoucherRepository voucherRepository;
    private CustomerApplicationService service;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        loyaltyRepository = mock(LoyaltyRepository.class);
        voucherRepository = mock(VoucherRepository.class);
        service = new CustomerApplicationService(customerRepository, loyaltyRepository);
    }

//    @Test
//    void shouldInitializeCustomerProfileCorrectly() {
//        // Arrange
//        UUID userId = UUID.randomUUID();
//        Customer customer = Customer.builder()
//                .customerId(CustomerId.generate())
//                .userId(userId)
//                .build();
//
//        Loyalty loyalty = Loyalty.createNew(customer.getId());
//
//        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
//        when(loyaltyRepository.save(any(Loyalty.class))).thenReturn(loyalty);
//
//        // Act
//        CustomerId result = service.initializeCustomerProfile(userId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(customer.getId(), result);
//        verify(customerRepository, times(1)).save(any(Customer.class));
//        verify(loyaltyRepository, times(1)).save(any(Loyalty.class));
//        System.out.println(customer.toString());
//        System.out.println(loyalty.toString());
//    }
}
