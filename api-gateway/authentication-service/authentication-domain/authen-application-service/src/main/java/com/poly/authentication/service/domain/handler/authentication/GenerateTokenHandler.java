package com.poly.authentication.service.domain.handler.authentication;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.poly.authentication.service.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static com.poly.domain.DomainConstants.JWT_SECRET;
import static com.poly.domain.DomainConstants.VALID_DURATION;

@Slf4j
@Component
public class GenerateTokenHandler {

    public String generateToken(User user) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getGmail())
                .issuer("nikka.io.vn")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", user.getRole().getRoleName())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(JWT_SECRET.getBytes()));

            return jwsObject.serialize();

        } catch (JOSEException e) {

            log.error("Can't generate token", e);

            throw new RuntimeException(e);
        }
    }
}
