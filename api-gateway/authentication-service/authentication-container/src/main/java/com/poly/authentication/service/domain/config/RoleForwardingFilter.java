package com.poly.authentication.service.domain.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@Slf4j
public class RoleForwardingFilter implements GlobalFilter, Ordered {

    public static final String X_USER_ROLES_HEADER = "X-User-Roles";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Lấy SecurityContext từ ReactiveSecurityContextHolder
        // Filter này phải chạy SAU khi Spring Security đã xác thực và đưa Authentication vào SecurityContext
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    if (authentication == null || !authentication.isAuthenticated()) {
                        // Nếu không có thông tin xác thực hoặc chưa được xác thực,
                        // tiếp tục chuỗi filter mà không thêm header vai trò.
                        // Điều này có thể xảy ra với các public endpoint.
                        log.debug("No authenticated user found in SecurityContext. Not adding X-User-Roles header.");
                        return chain.filter(exchange);
                    }

                    // Trích xuất các vai trò từ đối tượng Authentication
                    String roles = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(",")); // Nối các vai trò bằng dấu phẩy

                    // Tạo một request mới với header X-User-Roles
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header(X_USER_ROLES_HEADER, roles)
                            .build();

                    log.debug("Adding header {} with roles: {} for request to {}", X_USER_ROLES_HEADER, roles, modifiedRequest.getURI());

                    // Chuyển tiếp exchange với request đã được sửa đổi
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Xử lý trường hợp SecurityContext trống rỗng (ví dụ: request không được bảo vệ)
                    log.debug("SecurityContext is empty. Not adding X-User-Roles header.");
                    return chain.filter(exchange);
                }));
    }

    @Override
    public int getOrder() {
        // Đặt thứ tự của filter này.
        // Cần chạy sau các filter xác thực của Spring Security.
        // Ordered.HIGHEST_PRECEDENCE là giá trị thấp nhất (chạy đầu tiên).
        // authentication filter của Spring Security có order rất sớm (thường là Ordered.AUTHENTICATION_ORDER).
        // Chúng ta muốn filter này chạy sau đó.
        // Giá trị 0 hoặc dương thường sẽ chạy sau các filter của Spring Security.
        // Có thể thử các giá trị như -1 (để chạy ngay sau xác thực nếu có thể) hoặc 0.
        // Trong trường hợp này, cứ để 0 để chạy sau các filter core của Spring Boot/Security.
        return 0; // Đảm bảo chạy sau JwtAuthenticationFilter (mà được kích hoạt bởi SecurityConfig)
    }
}