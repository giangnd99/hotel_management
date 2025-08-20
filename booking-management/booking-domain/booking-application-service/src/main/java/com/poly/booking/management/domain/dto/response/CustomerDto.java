package com.poly.booking.management.domain.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
public class CustomerDto {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String address;
    private LocalDate dateOfBirth;
    private String sex;
    private boolean active;

}