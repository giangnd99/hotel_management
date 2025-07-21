package com.poly.customerdataaccess.repository;

import com.poly.customerdataaccess.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> findByUserId(UUID userId);
    Optional<CustomerEntity> findById(UUID userId);
}
