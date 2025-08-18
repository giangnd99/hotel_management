package com.poly.security.domain.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutRequest {

    private String token;
}
