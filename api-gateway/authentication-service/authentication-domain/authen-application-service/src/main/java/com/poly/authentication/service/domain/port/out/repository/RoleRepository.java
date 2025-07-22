package com.poly.authentication.service.domain.port.out.repository;

import com.poly.authentication.service.domain.entity.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findByName(String name);
}
