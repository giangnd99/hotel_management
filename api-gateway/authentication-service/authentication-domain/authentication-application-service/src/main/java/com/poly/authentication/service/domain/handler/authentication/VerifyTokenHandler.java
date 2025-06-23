package com.poly.authentication.management.domain.handler.authentication;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.poly.application.handler.AppException;
import com.poly.application.handler.ErrorCode;
import com.poly.domain.DomainConstants;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class VerifyTokenHandler {

    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(DomainConstants.JWT_SECRET.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh) ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(DomainConstants.REFRESH_TOKEN_VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()) : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED_EXCEPTION);


        return signedJWT;
    }
}
