package com.poly.authentication.service.domain.dto.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGGResponse {
    String id;

    String email;

    String fullName;

    String firstName;

    String lastName;

    String phone;

    String role;

    String avatar;

    String token;
}
