package com.poly.customerdataaccess.repository.impl;

import com.poly.customerdataaccess.entity.LoyaltyTransactionEntity;
import com.poly.customerdataaccess.mapper.LoyaltyTransactionDataMapper;
import com.poly.customerdataaccess.repository.LoyaltyTransactionJpaRepository;
import com.poly.customerdomain.model.entity.LoyaltyTransaction;
import com.poly.customerdomain.output.LoyaltyTransactionRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
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
        List<LoyaltyTransactionEntity> entities = loyaltyTransactionJpaRepository.findByLoyaltyPointId(loyaltyId);
        List<LoyaltyTransaction> loyaltyTransactions = entities.stream().map(LoyaltyTransactionDataMapper::toDomain).collect(Collectors.toList());
        return loyaltyTransactions;
    }
}
