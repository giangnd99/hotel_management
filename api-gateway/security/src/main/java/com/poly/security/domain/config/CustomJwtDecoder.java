package com.poly.security.domain.config;

import com.poly.domain.DomainConstants;
import com.poly.security.domain.dto.request.IntrospectRequest;
import com.poly.security.domain.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomJwtDecoder implements ReactiveJwtDecoder {

    private final AuthenticationService authenticationService;

    private ReactiveJwtDecoder reactiveNimbusJwtDecoder = null;


    @Override
    public Mono<Jwt> decode(String token) throws JwtException {

        log.info("JWT token: {}", token);

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
                        log.info("JWT token decoded successfully");
                        return reactiveNimbusJwtDecoder.decode(token);
                    } catch (JwtException e) {
                        log.error("JWT token decode failed", e);
                        return Mono.error(new JwtException("Token invalid"));
                    }
                }
        ).switchIfEmpty(Mono.error(new JwtException("Token invalid")));
    }
}
