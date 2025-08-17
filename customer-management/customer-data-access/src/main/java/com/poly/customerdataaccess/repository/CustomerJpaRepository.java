package com.poly.customerdataaccess.repository;

import com.poly.customerdataaccess.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> findByUserId(UUID userId);
    Optional<CustomerEntity> findById(UUID userId);
}
