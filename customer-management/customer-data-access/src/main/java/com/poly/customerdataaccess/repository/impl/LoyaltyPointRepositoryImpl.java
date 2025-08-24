package com.poly.customerdataaccess.repository.impl;

import com.poly.customerdataaccess.entity.LoyaltyPointEntity;
import com.poly.customerdataaccess.repository.LoyaltyPointJpaRepository;
import com.poly.customerdataaccess.mapper.LoyaltyPointDataMapper;
import com.poly.customerdomain.model.entity.LoyaltyPoint;
import com.poly.customerdomain.output.LoyaltyPointRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Component
public class LoyaltyPointRepositoryImpl implements LoyaltyPointRepository {

    private final LoyaltyPointJpaRepository loyaltyPointJpaRepository;

    public LoyaltyPointRepositoryImpl(LoyaltyPointJpaRepository loyaltyPointJpaRepository) {
        this.loyaltyPointJpaRepository = loyaltyPointJpaRepository;
    }

    @Override
    public LoyaltyPoint save(LoyaltyPoint loyaltyPoint) {
        LoyaltyPointEntity entity = LoyaltyPointDataMapper.toEntity(loyaltyPoint);
        LoyaltyPointEntity saved = loyaltyPointJpaRepository.save(entity);
        return LoyaltyPointDataMapper.toDomain(saved);
    }

    @Override
    public Optional<LoyaltyPoint> findByCustomerId(UUID customerId) {
        return loyaltyPointJpaRepository.findByCustomerId(customerId).map(LoyaltyPointDataMapper::toDomain);
    }
}
