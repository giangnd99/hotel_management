package com.poly.authentication.service.domain.dto.request.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdatedRequest {

    @Nullable
    @Email(message = "EMAIL_INVALID")
    private String email;

    @Nullable
    private String phone;

}
