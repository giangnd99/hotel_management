package com.poly.authentication.service.domain.implement;

import com.poly.authentication.service.domain.dto.reponse.user.UserResponse;
import com.poly.authentication.service.domain.dto.request.user.UserCreationRequest;
import com.poly.authentication.service.domain.dto.request.user.UserUpdatedRequest;
import com.poly.authentication.service.domain.entity.Role;
import com.poly.authentication.service.domain.entity.User;
import com.poly.authentication.service.domain.exception.AppException;
import com.poly.authentication.service.domain.exception.ErrorCode;
import com.poly.authentication.service.domain.handler.authentication.GenerateTokenHandler;
import com.poly.authentication.service.domain.mapper.UserMapper;
import com.poly.authentication.service.domain.port.in.service.UserService;
import com.poly.authentication.service.domain.port.out.httpclient.NotificationClient;
import com.poly.authentication.service.domain.port.out.repository.UserRepository;
import com.poly.authentication.service.domain.valueobject.Password;
import com.poly.dao.util.PageUtil;
import com.poly.domain.valueobject.ERole;
import com.poly.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GenerateTokenHandler generateTokenHandler;
    private final UserMapper userMapper;
    private final PageUtil pageUtil;
    private final PasswordEncoder passwordEncoder;
    private final NotificationClient notificationClient;

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        User userRequest = userRepository.findByEmail(request.getEmail()).orElseGet(() -> {
            User user = userMapper.toDomainEntity(request);
            user.changePassword(new Password(passwordEncoder.encode(request.getPassword())));
            user.addRole(Role.Builder.builder()
                    .name(ERole.ROLE_CUSTOMER)
                    .build());
            return userRepository.save(user);
        });
        String token = generateTokenHandler.generateToken(userRequest);
        userRequest.setToken(token);
        log.info("Token: {}", userRequest.getToken());
        return userMapper.toUserResponse(userRequest);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getUsers(int page) {

        Pageable pageable = pageUtil.createPageable(page);

        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse getMyInfo() {

        SecurityContext context = SecurityContextHolder.getContext();

        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email)

                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getUserById(UUID id) {

        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));

    }

    @Override
    @PostAuthorize("returnObject.email == authentication.name")
    @Transactional
    public UserResponse updateUser(UUID userId, UserUpdatedRequest request) {

        User userUpdating = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (userUpdating.getGmail().equals(request.getEmail()) || Objects.equals(request.getPhone(), userUpdating.getPhone())) {
            log.info("Email or phone is same as current user");
            return userMapper.toUserResponse(userUpdating);
        }
        userMapper.fromUpdatePhoneRequestToDomainEntity(request, userUpdating);

        userUpdating.setId(new UserId(userId));

        return userMapper.toUserResponse(userRepository.save(userUpdating));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User userResetPassword = userRepository.findByEmail(email)

                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String oldPassword = userResetPassword.getPassword().getValue();

        if (!passwordEncoder.matches(oldPassword, newPassword)) {

            userResetPassword.changePassword(new Password(passwordEncoder.encode(newPassword)));

            userRepository.save(userResetPassword);
            notificationClient.sendAccountInfo(email, newPassword);
        } else throw new AppException(ErrorCode.PASSWORD_INVALID);
    }


}

