package com.poly.authentication.service.dao.user.repository;

import com.poly.authentication.service.dao.user.entity.UserEntity;
import com.poly.authentication.service.dao.user.repository.custom.CustomUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID>, CustomUserRepository {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findAll(Pageable pageable);
}
