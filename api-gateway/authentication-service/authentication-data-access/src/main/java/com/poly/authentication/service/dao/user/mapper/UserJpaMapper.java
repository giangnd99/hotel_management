package com.poly.authentication.service.dao.user.mapper;

import com.poly.authentication.service.dao.role.entity.RoleEntity;
import com.poly.authentication.service.dao.role.mapper.RoleJpaMapper;
import com.poly.authentication.service.dao.role.repository.RoleJpaRepository;
import com.poly.authentication.service.dao.user.entity.UserEntity;
import com.poly.authentication.service.domain.entity.User;
import com.poly.authentication.service.domain.valueobject.Password;
import com.poly.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserJpaMapper {

    private final RoleJpaRepository roleJpaRepository;
    private final RoleJpaMapper roleJpaMapper;

    public UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(UUID.randomUUID())
                .email(user.getGmail())
                .password(user.getPassword().getValue())
                .phone(user.getPhone())
                .build();
    }

    public User toDomainEntity(UserEntity entity) {

        return User.Builder.builder()
                .id(new UserId((entity.getId())))
                .phone(entity.getPhone())
                .password(new Password(entity.getPassword()))
                .gmail(entity.getEmail())
                .role(roleJpaMapper.toDomainEntity(entity.getRole()))
                .build();
    }
}
