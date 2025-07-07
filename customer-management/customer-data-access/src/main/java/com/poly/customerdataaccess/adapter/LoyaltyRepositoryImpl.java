package com.poly.customerdataaccess.adapter;

import com.poly.customerdataaccess.entity.LoyaltyEntity;
import com.poly.customerdataaccess.jpa.LoyaltyJpaRepository;
import com.poly.customerdataaccess.mapper.LoyaltyDataMapper;
import com.poly.customerdomain.model.entity.Loyalty;
import com.poly.customerdomain.output.LoyaltyRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class LoyaltyRepositoryImpl implements LoyaltyRepository {

    private final LoyaltyJpaRepository loyaltyJpaRepository;

    public LoyaltyRepositoryImpl(LoyaltyJpaRepository loyaltyJpaRepository) {
        this.loyaltyJpaRepository = loyaltyJpaRepository;
    }

    @Override
    public Loyalty save(Loyalty loyalty) {
        LoyaltyEntity entity = LoyaltyDataMapper.toEntity(loyalty);
        LoyaltyEntity saved = loyaltyJpaRepository.save(entity);
        return LoyaltyDataMapper.toDomain(saved);
    }

    @Override
    public Optional<Loyalty> findByCustomerId(UUID customerId) {
        return loyaltyJpaRepository.findByCustomerId(customerId);
    }
}
