package com.poly.customerdataaccess.kafka.listener;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import com.poly.customerapplicationservice.port.input.CustomerUsecase;
import com.poly.customerdataaccess.kafka.listener.CustomerCreatedEventListener;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;

class CustomerKafkaListenerTest {

    @Mock
    private CustomerUsecase customerUsecase;

    @InjectMocks
    private CustomerCreatedEventListener listener;

    public CustomerKafkaListenerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCallInitializeCustomerProfileWhenMessageReceived() {
        // 1. Arrange
        CreateCustomerCommand command = new CreateCustomerCommand();
        command.setUserId(UUID.randomUUID());
        command.setFirstName("Nguyen");
        command.setLastName("Van A");
        command.setDateOfBirth(LocalDate.of(1990, 1, 1));
        command.setAddress(new com.poly.customerapplicationservice.shared.AddressDto("123", "Ward", "District", "City"));

        // 2. Act
        listener.listen(command);

        // 3. Assert
        verify(customerUsecase, times(1)).initializeCustomerProfile(command);
    }
}
