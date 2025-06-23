package com.poly.authentication.management.domain.handler.authentication;

import com.nimbusds.jose.JOSEException;
import com.poly.application.handler.AppException;
import com.poly.authentication.management.domain.dto.request.LogoutRequest;
import com.poly.authentication.management.domain.port.out.repository.InvalidatedTokenRepository;
import com.poly.authentication.service.domain.entity.Token;
import com.poly.authentication.service.domain.valueobject.TokenId;
import com.poly.domain.valueobject.DateCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class LogoutHandler {

    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final VerifyTokenHandler verifyTokenHandler;

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyTokenHandler.verifyToken(request.getToken(), true);

            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            Token invalidatedToken = Token.Builder.builder()
                    .id(new TokenId(request.getToken()))
                    .expiryTime(DateCustom.of(expiryTime)).build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }
}
