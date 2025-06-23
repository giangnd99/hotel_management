package com.poly.authentication.service.dao.token.adapter;

import com.poly.authentication.service.dao.token.mapper.ForgotPasswordTokenJpaMapper;
import com.poly.authentication.service.dao.token.repository.ForgotPasswordTokenRedisRepository;
import com.poly.authentication.service.domain.entity.Token;
import com.poly.authentication.service.domain.port.out.repository.ForgotPasswordTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ForgotPasswordTokenRepositoryImpl implements ForgotPasswordTokenRepository {

    private final ForgotPasswordTokenJpaMapper forgotPasswordTokenJpaMapper;
    private final ForgotPasswordTokenRedisRepository forgotPasswordTokenRedisRepository;

    @Override
    public Token save(Token forgotPasswordToken) {
        return forgotPasswordTokenJpaMapper.toDomainEntity(
                forgotPasswordTokenRedisRepository.save(
                        forgotPasswordTokenJpaMapper.toEntity(forgotPasswordToken)));
    }

    @Override
    public Optional<Token> findById(String id) {
        return forgotPasswordTokenRedisRepository.findById(id).map(forgotPasswordTokenJpaMapper::toDomainEntity);
    }

    @Override
    public void deleteById(String id) {
        forgotPasswordTokenRedisRepository.deleteById(id);
    }
}
