package com.poly.customerapplicationservice.dto;

import com.poly.customerdomain.model.entity.Loyalty;
import com.poly.customerdomain.model.entity.valueobject.LoyaltyId;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class LoyaltyDto {
    private UUID id;
    private BigDecimal points;
    private LocalDateTime lastUpdated;

    public static LoyaltyDto from(Loyalty  loyalty){
        LoyaltyDto loyaltyDto = new LoyaltyDto();
        loyaltyDto.setId(LoyaltyId.toUUID(loyalty.getId()));
        loyaltyDto.setPoints(loyalty.getPoints().getValue());
        loyaltyDto.setLastUpdated(loyalty.getLastUpdated());
        return loyaltyDto;
    }
}
