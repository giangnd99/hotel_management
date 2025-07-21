package com.poly.authentication.service.dao.token.adapter;

import com.poly.authentication.service.dao.token.mapper.TokenJpaMapper;
import com.poly.authentication.service.dao.token.repository.TokenJpaRepository;
import com.poly.authentication.service.domain.entity.Token;
import com.poly.authentication.service.domain.port.out.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final TokenJpaRepository tokenJpaRepository;
    private final TokenJpaMapper tokenJpaMapper;

    @Override
    public Token save(Token invalidatedToken) {
        return tokenJpaMapper.toDomainEntity(
                tokenJpaRepository.save(tokenJpaMapper.toJpaEntity(invalidatedToken)));}
}
