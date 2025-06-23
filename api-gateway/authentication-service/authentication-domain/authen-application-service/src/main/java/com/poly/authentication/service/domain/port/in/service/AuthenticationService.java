package com.poly.authentication.service.domain.port.in.service;

import com.poly.authentication.service.domain.dto.reponse.AuthenticationResponse;
import com.poly.authentication.service.domain.dto.reponse.IntrospectResponse;
import com.poly.authentication.service.domain.dto.reponse.OutboundUserResponse;
import com.poly.authentication.service.domain.dto.reponse.UserGGResponse;
import com.poly.authentication.service.domain.dto.request.AuthenticationRequest;
import com.poly.authentication.service.domain.dto.request.IntrospectRequest;
import com.poly.authentication.service.domain.dto.request.LogoutRequest;
import com.poly.authentication.service.domain.dto.request.RefreshRequest;
import com.poly.authentication.service.domain.entity.User;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    String generateToken(User user);

    IntrospectResponse introspect(IntrospectRequest token);

    UserGGResponse processGoogleAccount(OutboundUserResponse googleAccount);

    void logout(LogoutRequest token);

    AuthenticationResponse refreshToken(RefreshRequest refreshToken);
}
