package com.poly.authentication.service.domain.handler.authentication;

import com.poly.application.handler.AppException;
import com.poly.application.handler.ErrorCode;
import com.poly.authentication.service.domain.dto.reponse.AuthenticationResponse;
import com.poly.authentication.service.domain.dto.request.AuthenticationRequest;
import com.poly.authentication.service.domain.port.out.repository.UserRepository;
import com.poly.authentication.service.domain.entity.User;
import com.poly.authentication.service.domain.service.AuthDomainService;
import com.poly.authentication.service.domain.valueobject.Password;
import com.poly.service.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationHandler extends BaseHandler<AuthDomainService, UserRepository> {

    private final GenerateTokenHandler generateTokenHandler;

    public AuthenticationHandler(AuthDomainService service, UserRepository repository, GenerateTokenHandler generateTokenHandler) {
        super(service, repository);
        this.generateTokenHandler = generateTokenHandler;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        User user = repository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.checkPassword(new Password(authenticationRequest.getPassword()));

        var token = generateTokenHandler.generateToken(user);

        log.info("Token: {}", token);

        return AuthenticationResponse.builder().token(token).build();
    }
}
