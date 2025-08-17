package com.poly.customerdataaccess.repository;

import com.poly.customerdataaccess.entity.LoyaltyTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoyaltyTransactionJpaRepository extends JpaRepository<LoyaltyTransactionEntity, UUID> {
    List<LoyaltyTransactionEntity> findByLoyaltyPointId(UUID loyaltyPointId);
}
