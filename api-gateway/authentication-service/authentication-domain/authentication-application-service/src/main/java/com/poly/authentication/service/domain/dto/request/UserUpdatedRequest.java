package com.poly.authentication.management.domain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UserUpdatedRequest {

    private String fullName;

    private String phone;

    private boolean gender;

    private LocalDate birthday;

}
