package com.poly.authentication.service.domain.mapper;

import com.poly.authentication.service.domain.dto.reponse.user.UserResponse;
import com.poly.authentication.service.domain.dto.request.user.UserCreationRequest;
import com.poly.authentication.service.domain.dto.request.user.UserUpdatedRequest;
import com.poly.authentication.service.domain.entity.User;
import com.poly.authentication.service.domain.valueobject.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toDomainEntity(UserCreationRequest userCreationRequest) {
        return User.Builder.builder()
                .gmail(userCreationRequest.getEmail())
                .password(new Password(userCreationRequest.getPassword()))
                .phone(userCreationRequest.getPhone())
                .build();
    }
    public void fromUpdatePhoneRequestToDomainEntity(UserUpdatedRequest userUpdatedRequest, User user) {
        user.changePhone(userUpdatedRequest.getPhone());
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .email(user.getGmail())
                .phone(user.getPhone())
                .id(user.getId().getValue().toString())
                .build();
    }
}
