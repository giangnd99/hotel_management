package com.poly.customerdataaccess.repository.impl;

import com.poly.customerdataaccess.entity.LoyaltyTransactionEntity;
import com.poly.customerdataaccess.mapper.LoyaltyTransactionDataMapper;
import com.poly.customerdataaccess.repository.LoyaltyTransactionJpaRepository;
import com.poly.customerdomain.model.entity.LoyaltyTransaction;
import com.poly.customerdomain.output.LoyaltyTransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class LoyaltyTransactionRepositoryImpl implements LoyaltyTransactionRepository {

    private final LoyaltyTransactionJpaRepository loyaltyTransactionJpaRepository;

    public LoyaltyTransactionRepositoryImpl(LoyaltyTransactionJpaRepository loyaltyTransactionJpaRepository) {
        this.loyaltyTransactionJpaRepository = loyaltyTransactionJpaRepository;
    }

    @Override
    public LoyaltyTransaction save(LoyaltyTransaction loyaltyTransaction) {
        LoyaltyTransactionEntity loyaltyTransactionEntity = LoyaltyTransactionDataMapper.toEntity(loyaltyTransaction);
        LoyaltyTransactionEntity saved = loyaltyTransactionJpaRepository.save(loyaltyTransactionEntity);
        return LoyaltyTransactionDataMapper.toDomain(saved);
    }

    @Override
    public List<LoyaltyTransaction> findAllByLoyaltyId(UUID loyaltyId) {
        return loyaltyTransactionJpaRepository.findByLoyaltyPointId(loyaltyId).stream().map(LoyaltyTransactionDataMapper::toDomain).toList();
    }
}
