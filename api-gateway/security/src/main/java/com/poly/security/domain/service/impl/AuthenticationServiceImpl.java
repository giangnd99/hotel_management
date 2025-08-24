package com.poly.security.domain.service.impl;

import com.nimbusds.jose.JOSEException;
import com.poly.security.domain.dto.ApiResponse;
import com.poly.security.domain.dto.reponse.AuthenticationResponse;
import com.poly.security.domain.dto.reponse.IntrospectResponse;
import com.poly.security.domain.dto.reponse.UserGGResponse;
import com.poly.security.domain.dto.request.AuthenticationRequest;
import com.poly.security.domain.dto.request.IntrospectRequest;
import com.poly.security.domain.dto.request.LogoutRequest;
import com.poly.security.domain.dto.request.RefreshRequest;
import com.poly.security.domain.repository.feign.AuthenticationWebClient;
import com.poly.security.domain.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationWebClient authenticationClient;

    @Override
    public Mono<IntrospectResponse> introspect(IntrospectRequest request) {
        return authenticationClient.introspect(request)
                .flatMap(apiResponse -> handleResponse(apiResponse, IntrospectResponse.class));
    }

    @Override
    public Mono<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        return authenticationClient.authenticate(request)
                .flatMap(apiResponse -> handleResponse(apiResponse, AuthenticationResponse.class));
    }

    @Override
    public Mono<Void> logout(LogoutRequest request) {
        return authenticationClient.logout(request)
                .flatMap(apiResponse -> {
                    if (apiResponse.getCode() == 0) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new RuntimeException(apiResponse.getMessage()));
                    }
                });
    }

    @Override
    public Mono<AuthenticationResponse> refreshToken(RefreshRequest request) {
        return authenticationClient.refresh(request)
                .flatMap(apiResponse -> handleResponse(apiResponse, AuthenticationResponse.class));
    }

    @Override
    public Mono<UserGGResponse> handleGoogleCallback(Map<String, String> requestData) {
        return authenticationClient.handleGoogleCallback(requestData)
                .flatMap(apiResponse -> handleResponse(apiResponse, UserGGResponse.class));
    }

    /**
     * Hàm generic xử lý ApiResponse cho gọn.
     */
    private <T> Mono<T> handleResponse(ApiResponse<?> apiResponse, Class<T> clazz) {
        if (apiResponse.getCode() == 0 && clazz.isInstance(apiResponse.getResult())) {
            return Mono.just(clazz.cast(apiResponse.getResult()));
        } else {
            return Mono.error(new RuntimeException(apiResponse.getMessage()));
        }
    }
}
