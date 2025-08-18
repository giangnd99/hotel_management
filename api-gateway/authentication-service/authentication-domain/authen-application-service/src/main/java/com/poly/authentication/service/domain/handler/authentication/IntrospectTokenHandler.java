package com.poly.authentication.service.domain.handler.authentication;

import com.nimbusds.jose.JOSEException;

import com.poly.authentication.service.domain.dto.reponse.IntrospectResponse;
import com.poly.authentication.service.domain.dto.request.IntrospectRequest;
import com.poly.authentication.service.domain.exception.AppException;
import com.poly.authentication.service.domain.exception.AuthenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class IntrospectTokenHandler {

    private final VerifyTokenHandler verifyTokenHandler;

    public IntrospectResponse introspect(IntrospectRequest request) {

        var token = request.getToken();

        boolean isValid = true;

        try {
            verifyTokenHandler.verifyToken(token, false);
        } catch (AppException e) {

            isValid = false;

        } catch (ParseException | JOSEException e) {
            throw new AuthenException("Token invalid");
        }
        return IntrospectResponse.builder().valid(isValid).build();

    }
}
