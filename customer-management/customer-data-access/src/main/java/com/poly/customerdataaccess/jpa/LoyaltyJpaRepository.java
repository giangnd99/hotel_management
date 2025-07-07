package com.poly.customerdataaccess.jpa;

import com.poly.customerdataaccess.entity.LoyaltyEntity;
import com.poly.customerdomain.model.entity.Loyalty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LoyaltyJpaRepository extends JpaRepository<LoyaltyEntity, Integer> {
    Optional<Loyalty> findByCustomerId(UUID customerId);
}
