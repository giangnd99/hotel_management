package com.poly.authentication.service.domain.handler.authentication;

import com.nimbusds.jose.JOSEException;
import com.poly.authentication.service.domain.dto.request.auth.LogoutRequest;
import com.poly.authentication.service.domain.exception.AppException;
import com.poly.authentication.service.domain.exception.AuthenException;
import com.poly.authentication.service.domain.port.out.repository.TokenRepository;
import com.poly.authentication.service.domain.entity.Token;
import com.poly.authentication.service.domain.valueobject.TokenId;
import com.poly.domain.valueobject.DateCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class LogoutHandler {

    private final TokenRepository invalidatedTokenRepository;
    private final VerifyTokenHandler verifyTokenHandler;

    public void logout(LogoutRequest request) {
        try {
            var signToken = verifyTokenHandler.verifyToken(request.getToken(), true);

            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            Token invalidatedToken = Token.Builder.builder()
                    .id(new TokenId(request.getToken()))
                    .expiryTime(DateCustom.of(expiryTime)).build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException exception) {
            log.info("Token already expired");
        } catch (ParseException | JOSEException e) {
            log.error("Token invalid {}", e.getMessage());
            throw new AuthenException("Token invalid " + e.getMessage());
        }
    }
}
