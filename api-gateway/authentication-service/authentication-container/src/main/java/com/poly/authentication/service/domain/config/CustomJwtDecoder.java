package com.poly.authentication.service.domain.config;

import com.poly.authentication.service.domain.dto.request.IntrospectRequest;
import com.poly.authentication.service.domain.port.in.service.AuthenticationService;
import com.poly.domain.DomainConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements ReactiveJwtDecoder {

    private final AuthenticationService authenticationService;

    private ReactiveJwtDecoder reactiveNimbusJwtDecoder = null;


    @Override
    public Mono<Jwt> decode(String token) throws JwtException {

        return authenticationService.introspect(
                IntrospectRequest.builder().token(token).build()).flatMap(
                response -> {
                    if (!response.isValid()) {
                        return Mono.error(new JwtException("Token invalid"));
                    }
                    if (Objects.isNull(reactiveNimbusJwtDecoder)) {
                        SecretKeySpec secretKeySpec = new SecretKeySpec(DomainConstants.JWT_SECRET.getBytes(), "HmacSHA512");
                        reactiveNimbusJwtDecoder = NimbusReactiveJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
                    }
                    try {
                        return reactiveNimbusJwtDecoder.decode(token);
                    } catch (JwtException e) {
                        return Mono.error(new JwtException("Token invalid"));
                    }
                }
        ).switchIfEmpty(Mono.error(new JwtException("Token invalid")));
    }
}
