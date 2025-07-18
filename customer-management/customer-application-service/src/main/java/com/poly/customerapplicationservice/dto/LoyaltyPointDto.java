package com.poly.customerapplicationservice.dto;

import com.poly.customerdomain.model.entity.LoyaltyPoint;
import com.poly.customerdomain.model.entity.valueobject.LoyaltyId;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class LoyaltyPointDto {
    private UUID id;
    private BigDecimal points;
    private LocalDateTime lastUpdated;

    public static LoyaltyPointDto from(LoyaltyPoint loyaltyPoint){
        LoyaltyPointDto loyaltyPointDto = new LoyaltyPointDto();
        loyaltyPointDto.setId(LoyaltyId.toUUID(loyaltyPoint.getId()));
        loyaltyPointDto.setPoints(loyaltyPoint.getPoints().getValue());
        loyaltyPointDto.setLastUpdated(loyaltyPoint.getLastUpdated());
        return loyaltyPointDto;
    }
}
