package com.poly.customerapplicationservice.command;

import com.poly.customerapplicationservice.shared.AddressDto;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class CreateCustomerCommand {
    private UUID userId;
    private String firstName;
    private String lastName;
    private AddressDto address;
    private LocalDate dateOfBirth;
}