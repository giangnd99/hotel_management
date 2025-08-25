package com.poly.booking.management.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateCustomerCommand {

    private String password;
    private String email;
    private String phone;
    @Nullable
    private UUID userId;
    private String firstName;
    private String lastName;
    @Nullable
    private String address;
    private String sex;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
