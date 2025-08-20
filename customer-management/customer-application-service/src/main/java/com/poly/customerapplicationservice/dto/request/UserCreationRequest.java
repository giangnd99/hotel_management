package com.poly.customerapplicationservice.dto.request;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {

    private String email;

    private String password;

    private String phone;
}
