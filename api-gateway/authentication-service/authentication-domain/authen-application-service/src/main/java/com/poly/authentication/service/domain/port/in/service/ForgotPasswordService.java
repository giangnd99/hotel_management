package com.poly.authentication.service.domain.port.in.service;

import com.poly.authentication.service.domain.entity.Token;

import java.util.Optional;

public interface ForgotPasswordService {
    Optional<Token> getForgotPasswordToken(String token);

    String createForgotPasswordToken(String email);

    boolean validateToken(String token);

    void deleteToken(String token);
}
