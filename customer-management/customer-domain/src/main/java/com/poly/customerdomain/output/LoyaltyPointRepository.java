package com.poly.customerdomain.output;

import com.poly.customerdomain.model.entity.LoyaltyPoint;

import java.util.Optional;
import java.util.UUID;

public interface LoyaltyPointRepository {
    LoyaltyPoint save(LoyaltyPoint loyaltyPoint);
    Optional<LoyaltyPoint> findByCustomerId(UUID customerId);
}
