package com.poly.authentication.service.domain.port.out.repository;

import com.poly.authentication.service.domain.entity.Token;

import java.util.Optional;

public interface ForgotPasswordTokenRepository {

    Token save(Token forgotPasswordToken);

    Optional<Token> findById(String id);

    void deleteById(String id);
}
