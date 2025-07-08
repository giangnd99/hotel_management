package com.poly.authentication.service.domain.port.out.repository;

import com.poly.authentication.service.domain.entity.Token;

public interface TokenRepository {
    Token save(Token invalidatedToken);
}
