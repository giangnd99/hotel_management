package com.poly.customerdomain.output;

import com.poly.customerdomain.model.entity.Loyalty;

public interface LoyaltyRepository {
    Loyalty save(Loyalty loyalty);
}
