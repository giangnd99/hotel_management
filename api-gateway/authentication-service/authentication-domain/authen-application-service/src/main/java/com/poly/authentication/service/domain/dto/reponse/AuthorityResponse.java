package com.poly.authentication.service.domain.dto.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorityResponse {

    Integer userId;
    List<String> authorities;
    String usersFullName;
}
