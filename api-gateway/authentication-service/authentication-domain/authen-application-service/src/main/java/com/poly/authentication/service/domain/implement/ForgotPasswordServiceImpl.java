package com.poly.authentication.service.domain.implement;

import com.poly.authentication.service.domain.dto.reponse.authen.IntrospectResponse;
import com.poly.authentication.service.domain.dto.request.auth.IntrospectRequest;
import com.poly.authentication.service.domain.entity.Token;
import com.poly.authentication.service.domain.entity.User;
import com.poly.authentication.service.domain.exception.AppException;
import com.poly.authentication.service.domain.exception.ErrorCode;
import com.poly.authentication.service.domain.handler.authentication.GenerateTokenHandler;
import com.poly.authentication.service.domain.port.in.service.AuthenticationService;
import com.poly.authentication.service.domain.port.in.service.ForgotPasswordService;
import com.poly.authentication.service.domain.port.out.httpclient.NotificationClient;
import com.poly.authentication.service.domain.port.out.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final GenerateTokenHandler generateTokenHandler;
    private final AuthenticationService authenticationService;
    private final NotificationClient notificationClient;

    @Override
    public Optional<Token> getForgotPasswordToken(String token) {
        return Optional.empty();
    }

    @Override
    public String createForgotPasswordToken(String email) {
        try {
            User userExiting = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


            String token = generateTokenHandler.generateToken(userExiting);

            log.info("Token: {}", token);
            notificationClient.sendAccountInfo(email, token);
            return "Đã gửi password code reset qua email ! vui lòng kiểm tra email đã đăng ký của bạn!";
        }catch (AppException e){
            log.error("User not existed {}", e.getMessage());

            return "Lỗi khi gửi email !";
        }
    }

    @Override
    public boolean validateToken(String token) {
        IntrospectResponse response = authenticationService.introspect(IntrospectRequest.builder().token(token).build());
        return response.isValid();
    }

    @Override
    public void deleteToken(String token) {

    }
}
