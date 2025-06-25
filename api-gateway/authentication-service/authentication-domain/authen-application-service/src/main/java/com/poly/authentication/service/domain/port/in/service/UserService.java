package com.poly.authentication.service.domain.port.in.service;

import com.poly.authentication.service.domain.dto.reponse.UserResponse;
import com.poly.authentication.service.domain.dto.request.UserCreationRequest;
import com.poly.authentication.service.domain.dto.request.UserUpdatedRequest;
import com.poly.authentication.service.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);

    Page<UserResponse> getUsers(int page);

    UserResponse getUserById(UUID id);

    UserResponse updateUser(UUID userId, UserUpdatedRequest request);

    Mono<User> getUserByEmail(String email);

    UserResponse getMyInfo();

    void deleteUser(UUID userId);

    void updatePassword(String email, String newPassword);

}
