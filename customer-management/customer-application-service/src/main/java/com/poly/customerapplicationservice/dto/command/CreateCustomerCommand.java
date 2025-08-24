package com.poly.customerapplicationservice.dto.command;

import com.poly.customerapplicationservice.shared.AddressDto;
import com.poly.customerdomain.model.entity.valueobject.Sex;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateCustomerCommand {

    private String password;
    private String email;
    private String phone;
    @Nullable
    private UUID userId;
    private String firstName;
    private String lastName;
    @Nullable
    private AddressDto address;
    private String sex;
    private LocalDate dateOfBirth;
}