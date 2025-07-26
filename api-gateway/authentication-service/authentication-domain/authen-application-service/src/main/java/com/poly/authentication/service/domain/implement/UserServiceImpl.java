package com.poly.authentication.service.domain.implement;

import com.poly.authentication.service.domain.dto.reponse.UserResponse;
import com.poly.authentication.service.domain.dto.request.UserCreationRequest;
import com.poly.authentication.service.domain.dto.request.UserUpdatedRequest;
import com.poly.authentication.service.domain.entity.Role;
import com.poly.authentication.service.domain.entity.Token;
import com.poly.authentication.service.domain.entity.User;
import com.poly.authentication.service.domain.exception.AppException;
import com.poly.authentication.service.domain.exception.ErrorCode;
import com.poly.authentication.service.domain.handler.authentication.GenerateTokenHandler;
import com.poly.authentication.service.domain.mapper.UserMapper;
import com.poly.authentication.service.domain.port.in.service.UserService;
import com.poly.authentication.service.domain.port.out.repository.UserRepository;
import com.poly.authentication.service.domain.valueobject.Password;
import com.poly.dao.util.PageUtil;
import com.poly.domain.valueobject.ERole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GenerateTokenHandler generateTokenHandler;
    private final UserMapper userMapper;
    private final PageUtil pageUtil;

    @Override
    public UserResponse createUser(UserCreationRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new AppException(ErrorCode.USER_EXISTED);

        }
        User user = userMapper.toDomainEntity(request);
        user.addRole(Role.Builder.builder()
                .name(ERole.ROLE_CUSTOMER)
                .build());
        User savedUser = userRepository.save(user);
        String token = generateTokenHandler.generateToken(savedUser);
        savedUser.setToken(token);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponse> getUsers(int page) {

        Pageable pageable = pageUtil.createPageable(page);

        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse getMyInfo() {

        var context = SecurityContextHolder.getContext();

        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email)

                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse getUserById(UUID id) {

        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));

    }

    @Override
    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse updateUser(UUID userId, UserUpdatedRequest request) {

        User userUpdating = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.fromUpdatePhoneRequestToDomainEntity(request, userUpdating);

        return userMapper.toUserResponse(userRepository.save(userUpdating));
    }

    @Override
    public Mono<User> getUserByEmail(String email) {
        return Mono.just(userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
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

        userResetPassword.changePassword(new Password(newPassword));

        userRepository.save(userResetPassword);
    }


}

