package com.poly.customerdomain.output;

import com.poly.customerdomain.model.entity.Loyalty;

import java.util.Optional;
import java.util.UUID;

public interface LoyaltyRepository {
    Loyalty save(Loyalty loyalty);
    Optional<Loyalty> findByCustomerId(UUID customerId);
}
