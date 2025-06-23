package com.poly.authentication.management.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    private String email;

    @NotBlank(message = "PASSWORD_CANNOT_BE_NULL")
    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;

    @NotBlank(message = "FIELD_BLANK")
    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private String phone;

    private Boolean gender;

    public String getFullName() {
        return (lastName != null ? lastName : "") + " " + (firstName != null ? firstName : "");
    }

}
