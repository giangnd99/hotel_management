package com.poly.customerdataaccess.repository;

import com.poly.customerdataaccess.entity.LoyaltyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoyaltyJpaRepository extends JpaRepository<LoyaltyEntity, Integer> {
    Optional<LoyaltyEntity> findByCustomerId(UUID customerId);
}
