package com.poly.authentication.service.domain.port.in.service;

import com.poly.authentication.service.domain.dto.reponse.authen.AuthenticationResponse;
import com.poly.authentication.service.domain.dto.reponse.authen.IntrospectResponse;
import com.poly.authentication.service.domain.dto.reponse.user.OutboundUserResponse;
import com.poly.authentication.service.domain.dto.reponse.user.UserGGResponse;
import com.poly.authentication.service.domain.dto.request.auth.AuthenticationRequest;
import com.poly.authentication.service.domain.dto.request.auth.IntrospectRequest;
import com.poly.authentication.service.domain.dto.request.auth.LogoutRequest;
import com.poly.authentication.service.domain.dto.request.auth.RefreshRequest;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    IntrospectResponse introspect(IntrospectRequest token);

    UserGGResponse processGoogleAccount(OutboundUserResponse googleAccount);

    void logout(LogoutRequest token);

    AuthenticationResponse refreshToken(RefreshRequest refreshToken);
}
