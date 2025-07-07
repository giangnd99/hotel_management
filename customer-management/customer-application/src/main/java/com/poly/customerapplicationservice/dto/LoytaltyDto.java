package com.poly.customerapplicationservice.dto;

import com.poly.customerdomain.model.entity.Loyalty;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
public class LoytaltyDto {
    private UUID id;
    private BigDecimal points;
    private LocalDateTime lastUpdated;

    public static LoytaltyDto from(Loyalty  loyalty){
        LoytaltyDto loyaltyDto = new LoytaltyDto();
        loyalty.setId(loyalty.getId());
        loyalty.setPoints(loyalty.getPoints());
        loyaltyDto.setLastUpdated(loyalty.getLastUpdated());
        return loyaltyDto;
    }
}
