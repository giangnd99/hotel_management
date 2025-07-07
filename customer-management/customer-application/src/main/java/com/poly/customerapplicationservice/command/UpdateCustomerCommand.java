package com.poly.customerapplicationservice.command;

import com.poly.customerapplicationservice.shared.AddressDto;

import java.time.LocalDate;
import java.util.UUID;

public class UpdateCustomerCommand {
    private UUID userId;
    private String firstName;
    private String lastName;
    private AddressDto address;
    private LocalDate dateOfBirth;
}
