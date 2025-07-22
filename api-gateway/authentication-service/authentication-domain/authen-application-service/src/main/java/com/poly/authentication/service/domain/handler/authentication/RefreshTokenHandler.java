package com.poly.authentication.service.domain.handler.authentication;

import com.nimbusds.jose.JOSEException;
import com.poly.authentication.service.domain.dto.reponse.AuthenticationResponse;
import com.poly.authentication.service.domain.dto.request.RefreshRequest;
import com.poly.authentication.service.domain.entity.Token;
import com.poly.authentication.service.domain.exception.AppException;
import com.poly.authentication.service.domain.exception.ErrorCode;
import com.poly.authentication.service.domain.port.out.repository.TokenRepository;
import com.poly.authentication.service.domain.port.out.repository.UserRepository;
import com.poly.authentication.service.domain.valueobject.TokenId;
import com.poly.domain.valueobject.DateCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class RefreshTokenHandler {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final VerifyTokenHandler verifyTokenHandler;
    private final GenerateTokenHandler generateToken;

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyTokenHandler.verifyToken(request.getToken(), true);

        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        Token invalidatedToken = Token.Builder.builder()
                .id(new TokenId(request.getToken()))
                .expiryTime(DateCustom.of(expiryTime))
                .build();

        tokenRepository.save(invalidatedToken);

        var email = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION));

        var token = generateToken.generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }
}
