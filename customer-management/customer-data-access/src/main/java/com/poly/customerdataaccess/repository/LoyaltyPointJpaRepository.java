package com.poly.customerdataaccess.repository;

import com.poly.customerdataaccess.entity.LoyaltyPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoyaltyPointJpaRepository extends JpaRepository<LoyaltyPointEntity, Integer> {
    Optional<LoyaltyPointEntity> findByCustomerId(UUID customerId);
}
