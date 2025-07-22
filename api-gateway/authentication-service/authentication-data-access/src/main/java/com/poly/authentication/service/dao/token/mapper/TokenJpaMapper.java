package com.poly.authentication.service.dao.token.mapper;

import com.poly.authentication.service.dao.token.entity.TokenEntity;
import com.poly.authentication.service.domain.entity.Token;
import com.poly.authentication.service.domain.valueobject.TokenId;
import com.poly.domain.valueobject.DateCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenJpaMapper {

    public Token toDomainEntity(TokenEntity entity) {
        return Token.Builder.builder()
                .id(new TokenId(entity.getId()))
                .expiryTime(DateCustom.of(entity.getExpiryTime()))
                .build();
    }

    public TokenEntity toJpaEntity(Token token) {
        return TokenEntity.builder()
                .id(token.getId().getValue())
                .expiryTime(token.getExpiryTime().toDate())
                .build();
    }
}
