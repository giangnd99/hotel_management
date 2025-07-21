package com.poly.customerdataaccess.repository;

import com.poly.customerdataaccess.entity.LoyaltyTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoyaltyTransactionJpaRepository extends JpaRepository<LoyaltyTransactionEntity, UUID> {
    List<LoyaltyTransactionEntity> findByLoyaltyPointId(UUID loyaltyPointId);
}
