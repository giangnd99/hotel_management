package com.poly.security.domain.repository.feign;

import com.nimbusds.jose.JOSEException;
import com.poly.security.domain.dto.ApiResponse;
import com.poly.security.domain.dto.reponse.AuthenticationResponse;
import com.poly.security.domain.dto.reponse.IntrospectResponse;
import com.poly.security.domain.dto.reponse.UserGGResponse;
import com.poly.security.domain.dto.request.AuthenticationRequest;
import com.poly.security.domain.dto.request.IntrospectRequest;
import com.poly.security.domain.dto.request.LogoutRequest;
import com.poly.security.domain.dto.request.RefreshRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Map;

@Component
public class AuthenticationWebClient {

    private final WebClient webClient;

    public AuthenticationWebClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8092/auth").build();
    }

    public Mono<ApiResponse<IntrospectResponse>> introspect(IntrospectRequest request) {
        return webClient.post()
                .uri("/introspect")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<IntrospectResponse>>() {});
    }

    public Mono<ApiResponse<AuthenticationResponse>> authenticate(AuthenticationRequest request) {
        return webClient.post()
                .uri("/token")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<AuthenticationResponse>>() {});
    }

    public Mono<ApiResponse<Void>> logout(LogoutRequest request) {
        return webClient.post()
                .uri("/logout")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Void>>() {});
    }

    public Mono<ApiResponse<AuthenticationResponse>> refresh(RefreshRequest request) {
        return webClient.post()
                .uri("/refresh")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<AuthenticationResponse>>() {});
    }

    public Mono<ApiResponse<UserGGResponse>> handleGoogleCallback(Map<String, String> requestData) {
        return webClient.post()
                .uri("/callback")
                .bodyValue(requestData)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserGGResponse>>() {});
    }

}
