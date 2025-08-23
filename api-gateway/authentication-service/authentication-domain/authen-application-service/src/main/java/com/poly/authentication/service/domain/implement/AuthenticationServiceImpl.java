package com.poly.authentication.service.domain.implement;

import com.poly.authentication.service.domain.dto.reponse.authen.AuthenticationResponse;
import com.poly.authentication.service.domain.dto.reponse.authen.IntrospectResponse;
import com.poly.authentication.service.domain.dto.reponse.user.OutboundUserResponse;
import com.poly.authentication.service.domain.dto.reponse.user.UserGGResponse;
import com.poly.authentication.service.domain.dto.request.auth.AuthenticationRequest;
import com.poly.authentication.service.domain.dto.request.auth.IntrospectRequest;
import com.poly.authentication.service.domain.dto.request.auth.LogoutRequest;
import com.poly.authentication.service.domain.dto.request.auth.RefreshRequest;
import com.poly.authentication.service.domain.handler.authentication.*;
import com.poly.authentication.service.domain.port.in.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationHandler authenticationHandler;

    private final ProcessGoogleAccountHandler processGoogleAccountHandler;
    private final LogoutHandler logoutHandler;
    private final RefreshTokenHandler refreshTokenHandler;
    private final IntrospectTokenHandler introspectTokenHandler;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        return authenticationHandler.authenticate(authenticationRequest);
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest token) {
        return introspectTokenHandler.introspect(token);
    }

    @Override
    public UserGGResponse processGoogleAccount(OutboundUserResponse googleAccount) {
        return processGoogleAccountHandler.processGoogleAccount(googleAccount);
    }

    @Override
    public void logout(LogoutRequest token) {
        logoutHandler.logout(token);
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest refreshToken) {
        return null;
    }
}
