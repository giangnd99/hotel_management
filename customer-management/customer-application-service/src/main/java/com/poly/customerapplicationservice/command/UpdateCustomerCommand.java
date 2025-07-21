package com.poly.customerapplicationservice.command;

import com.poly.customerapplicationservice.shared.AddressDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class UpdateCustomerCommand {
    private UUID userId;
    private String firstName;
    private String lastName;
    private AddressDto address;
    private String image;
    private LocalDate dateOfBirth;
}
