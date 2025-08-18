package com.poly.security.domain.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        if (source.getClaimAsStringList("authorities") == null) {
            return List.of();
        }
        log.info("JWT claims: {}", source.getClaims());
        return source.getClaimAsStringList("authorities").stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}