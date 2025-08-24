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
        
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    if (authentication == null || !authentication.isAuthenticated()) {

                        log.debug("No authenticated user found in SecurityContext. Not adding X-User-Roles header.");
                        return chain.filter(exchange);
                    }

                    String roles = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(","));

                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header(X_USER_ROLES_HEADER, roles)
                            .build();

                    log.debug("Adding header {} with roles: {} for request to {}", X_USER_ROLES_HEADER, roles, modifiedRequest.getURI());

                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("SecurityContext is empty. Not adding X-User-Roles header.");
                    return chain.filter(exchange);
                }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}