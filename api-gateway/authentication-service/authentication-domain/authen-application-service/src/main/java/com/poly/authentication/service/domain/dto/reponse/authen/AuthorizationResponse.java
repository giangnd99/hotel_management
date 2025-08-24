package com.poly.authentication.service.domain.dto.reponse.authen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorizationResponse {

    private List<AuthorityResponse> usersAndAuthorities;
}
