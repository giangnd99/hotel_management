package com.poly.customerdataaccess.mapper;

import com.poly.customerdataaccess.entity.LoyaltyEntity;
import com.poly.customerdomain.model.entity.Loyalty;
import com.poly.customerdomain.model.entity.valueobject.LoyaltyId;
import com.poly.customerdomain.model.entity.valueobject.Point;
import com.poly.domain.valueobject.CustomerId;

import java.util.UUID;

public class LoyaltyDataMapper {

    public static LoyaltyEntity toEntity(Loyalty domain) {
        LoyaltyEntity entity = new LoyaltyEntity();
        entity.setCustomerId(domain.getCustomerId().getValue());
        entity.setPoints(domain.getPoints().getValue());
        entity.setLastUpdated(domain.getLastUpdated());
        return entity;
    }

    public static Loyalty toDomain(LoyaltyEntity entity) {
        return new Loyalty.Builder(new CustomerId(entity.getCustomerId()))
                .id(new LoyaltyId(entity.getId()))
                .points(new Point(entity.getPoints()))
                .lastUpdated(entity.getLastUpdated()) // nếu có field này trong LoyaltyEntity
                .build();
    }

}