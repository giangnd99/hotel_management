package com.poly.authentication.service.domain.port.out.repository;

import com.poly.authentication.service.domain.entity.User;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    User save(User user);

    boolean existsByEmail(@Size(min = 3, message = "USERNAME_INVALID") String email);

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(UUID userId);

    void deleteById(UUID userId);
}
