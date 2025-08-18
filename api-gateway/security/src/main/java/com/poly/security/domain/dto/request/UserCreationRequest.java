package com.poly.security.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    private String phone;
}
