package com.poly.authentication.management.domain.port.in.service;

import com.poly.authentication.management.domain.dto.reponse.AuthenticationResponse;
import com.poly.authentication.management.domain.dto.reponse.IntrospectResponse;
import com.poly.authentication.management.domain.dto.reponse.OutboundUserResponse;
import com.poly.authentication.management.domain.dto.reponse.UserGGResponse;
import com.poly.authentication.management.domain.dto.request.AuthenticationRequest;
import com.poly.authentication.management.domain.dto.request.IntrospectRequest;
import com.poly.authentication.management.domain.dto.request.LogoutRequest;
import com.poly.authentication.management.domain.dto.request.RefreshRequest;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    String generateToken(String username, String role);

    IntrospectResponse introspect(IntrospectRequest token);

    UserGGResponse processGoogleAccount(OutboundUserResponse googleAccount);

    void logout(LogoutRequest token);

    AuthenticationResponse refreshToken(RefreshRequest refreshToken);
}
