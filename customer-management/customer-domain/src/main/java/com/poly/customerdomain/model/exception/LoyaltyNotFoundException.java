package com.poly.customerdomain.model.exception;

import java.util.UUID;

public class LoyaltyNotFoundException extends DomainException{
    public LoyaltyNotFoundException(UUID loyaltyId) {
        super("Loyalty Id %s không tồn tại .".formatted(loyaltyId));
    }
    @Override
    public String getErrorCode() {
        return "LOYALTY_NOT_FOUND";
    }
}
