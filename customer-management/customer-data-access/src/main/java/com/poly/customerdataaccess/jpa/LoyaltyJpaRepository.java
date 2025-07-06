package com.poly.customerdataaccess.jpa;

import com.poly.customerdataaccess.entity.LoyaltyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoyaltyJpaRepository extends JpaRepository<LoyaltyEntity, Integer> {
}
