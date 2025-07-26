package com.poly.authentication.service.domain.handler.authentication;

import com.poly.authentication.service.domain.dto.reponse.OutboundUserResponse;
import com.poly.authentication.service.domain.dto.reponse.UserGGResponse;
import com.poly.authentication.service.domain.port.out.repository.UserRepository;
import com.poly.authentication.service.domain.entity.Role;
import com.poly.authentication.service.domain.entity.User;
import com.poly.authentication.service.domain.valueobject.Password;
import com.poly.domain.valueobject.ERole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessGoogleAccountHandler {

    private final UserRepository userRepository;
    private final GenerateTokenHandler generateTokenHandler;

    public UserGGResponse processGoogleAccount(OutboundUserResponse googleAccount) {
        User userOauth2 = userRepository.findByEmail(googleAccount.getEmail()).orElseGet(() -> {

            Role role = Role.Builder.builder()
                    .name(ERole.ROLE_CUSTOMER)
                    .build();

            User newUser = User.Builder.builder()
                    .gmail(googleAccount.getEmail())
                    .role(role)
                    .password(new Password(new Random().nextInt(1000000) + ""))
                    .phone("0123456789")
                    .build();

            return userRepository.save(newUser);
        });

        UserGGResponse userResponse = UserGGResponse.builder()
                .email(userOauth2.getGmail())
                .avatar(googleAccount.getPicture())
                .fullName(googleAccount.getName())
                .id(userOauth2.getId().getValue().toString())
                .build();

        userResponse.setToken(generateTokenHandler.generateToken(userOauth2));

        return userResponse;
    }
}
