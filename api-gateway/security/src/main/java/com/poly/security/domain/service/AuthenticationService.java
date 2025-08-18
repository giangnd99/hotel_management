package com.poly.security.domain.service;

import com.poly.security.domain.dto.reponse.AuthenticationResponse;
import com.poly.security.domain.dto.reponse.IntrospectResponse;
import com.poly.security.domain.dto.reponse.UserGGResponse;
import com.poly.security.domain.dto.request.AuthenticationRequest;
import com.poly.security.domain.dto.request.IntrospectRequest;
import com.poly.security.domain.dto.request.LogoutRequest;
import com.poly.security.domain.dto.request.RefreshRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface AuthenticationService {

    Mono<IntrospectResponse> introspect(IntrospectRequest request);
    Mono<AuthenticationResponse> authenticate(AuthenticationRequest request);
    Mono<Void> logout(LogoutRequest request);
    Mono<AuthenticationResponse> refreshToken(RefreshRequest request);
    Mono<UserGGResponse> handleGoogleCallback(Map<String, String> requestData);
}
