package com.poly.authentication.service.dao.token.mapper;

import com.poly.authentication.service.dao.token.entity.ForgotPasswordTokenEntity;
import com.poly.authentication.service.dao.token.entity.TokenEntity;
import com.poly.authentication.service.domain.entity.Token;
import com.poly.authentication.service.domain.valueobject.TokenId;
import com.poly.domain.valueobject.DateCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@RequiredArgsConstructor
public class ForgotPasswordTokenJpaMapper {

    public Token toDomainEntity(ForgotPasswordTokenEntity entity) {
        return Token.Builder.builder()
                .id(new TokenId(entity.getId()))
                .expiryTime(DateCustom.fromTimestamp(entity.getExpiresTime()))
                .build();
    }

    public ForgotPasswordTokenEntity toEntity(Token token) {
        return ForgotPasswordTokenEntity.builder()
                .id(token.getId().getValue())
                .expiresTime(token.getExpiryTime().toTimestamp())
                .build();
    }
}
