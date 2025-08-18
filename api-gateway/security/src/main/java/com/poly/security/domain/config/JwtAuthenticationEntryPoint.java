package com.poly.security.domain.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.poly.security.domain.dto.ApiResponse;
import com.poly.security.domain.dto.ErrorCode;
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
        exchange.getResponse().setStatusCode(errorCode.getHttpStatus());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Tạo đối tượng phản hồi lỗi
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        // Sử dụng ObjectMapper để chuyển đổi đối tượng thành JSON byte array
        ObjectMapper mapper = new ObjectMapper();

        return exchange.getResponse().writeWith(Mono.fromCallable(() -> {
            try {
                return exchange.getResponse().bufferFactory().wrap(
                        mapper.writeValueAsBytes(apiResponse)
                );
            } catch (Exception e) {
                log.error("Error writing authentication error response", e);
                // Trả về lỗi nếu quá trình ghi lỗi cũng thất bại
                throw new IllegalStateException("Failed to write authentication error response", e);
            }
        }));
    }
}