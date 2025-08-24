package com.poly.authentication.service.domain.dto.request.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults( level = AccessLevel.PRIVATE)
public class IntrospectRequest {

    String token;
}
