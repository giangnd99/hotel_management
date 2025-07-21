package com.poly.customerdomain.output;

import com.poly.customerdomain.model.entity.LoyaltyTransaction;

import java.util.List;
import java.util.UUID;

public interface LoyaltyTransactionRepository {
    LoyaltyTransaction save(LoyaltyTransaction loyaltyTransaction);
    List<LoyaltyTransaction> findAllByLoyaltyId(UUID loyaltyId);
}
