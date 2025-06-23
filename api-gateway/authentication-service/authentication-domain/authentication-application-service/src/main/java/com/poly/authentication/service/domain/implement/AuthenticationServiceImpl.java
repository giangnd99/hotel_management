package com.poly.authentication.management.domain.implement;

import com.poly.authentication.management.domain.dto.reponse.AuthenticationResponse;
import com.poly.authentication.management.domain.dto.reponse.IntrospectResponse;
import com.poly.authentication.management.domain.dto.reponse.OutboundUserResponse;
import com.poly.authentication.management.domain.dto.reponse.UserGGResponse;
import com.poly.authentication.management.domain.dto.request.AuthenticationRequest;
import com.poly.authentication.management.domain.dto.request.IntrospectRequest;
import com.poly.authentication.management.domain.dto.request.LogoutRequest;
import com.poly.authentication.management.domain.dto.request.RefreshRequest;
import com.poly.authentication.management.domain.port.in.service.AuthenticationService;

public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        return null;
    }

    @Override
    public String generateToken(String username, String role) {
        return "";
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest token) {
        return null;
    }

    @Override
    public UserGGResponse processGoogleAccount(OutboundUserResponse googleAccount) {
        return null;
    }

    @Override
    public void logout(LogoutRequest token) {

    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest refreshToken) {
        return null;
    }
}
