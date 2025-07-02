package com.poly.authentication.service.dao.role.adapter;

import com.poly.authentication.service.dao.role.mapper.RoleJpaMapper;
import com.poly.authentication.service.dao.role.repository.RoleJpaRepository;
import com.poly.authentication.service.domain.entity.Role;
import com.poly.authentication.service.domain.port.out.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RoleJpaMapper roleJpaMapper;

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name).map(roleJpaMapper::toDomainEntity);
    }
}
