package com.poly.authentication.service.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.authentication.service.domain.dto.ApiResponse;
import com.poly.authentication.service.domain.exception.AppException;
import com.poly.authentication.service.domain.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return Mono.fromRunnable(() -> {
            exchange.getResponse().setStatusCode(errorCode.getHttpStatus());
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .build();
            ObjectMapper mapper = new ObjectMapper();

            try {
                exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(
                        mapper.writeValueAsBytes(apiResponse)
                ))).subscribe();
            } catch (Exception e) {
                log.error("Error when writing response: {}", e.getMessage());
                throw new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION);
            }
        });
    }
}
