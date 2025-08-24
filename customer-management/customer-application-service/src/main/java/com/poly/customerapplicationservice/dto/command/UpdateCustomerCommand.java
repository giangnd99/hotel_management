package com.poly.customerapplicationservice.dto.command;

import com.poly.customerapplicationservice.shared.AddressDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class UpdateCustomerCommand {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private AddressDto address;
    private LocalDate dateOfBirth;
    private String sex;
    private boolean active;
}
