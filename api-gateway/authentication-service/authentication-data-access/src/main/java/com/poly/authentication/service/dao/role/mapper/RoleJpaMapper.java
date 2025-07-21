package com.poly.authentication.service.dao.role.mapper;

import com.poly.authentication.service.dao.role.entity.RoleEntity;
import com.poly.authentication.service.domain.entity.Role;
import com.poly.authentication.service.domain.valueobject.RoleId;
import com.poly.domain.valueobject.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleJpaMapper {

    public Role toDomainEntity(RoleEntity entity) {
        return Role.Builder.builder()
                .id(new RoleId(entity.getId()))
                .name(ERole.valueOf(entity.getName()))
                .build();
    }

    public RoleEntity toEntity(Role role) {
        return RoleEntity.builder()
                .id(role.getId().getValue())
                .name(role.getRoleName())
                .build();
    }
}
