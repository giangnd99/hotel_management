package com.poly.customerdataaccess.mapper;

import com.poly.customerdataaccess.entity.LoyaltyPointEntity;
import com.poly.customerdomain.model.entity.LoyaltyPoint;
import com.poly.customerdomain.model.entity.valueobject.LoyaltyId;
import com.poly.customerdomain.model.entity.valueobject.Point;
import com.poly.domain.valueobject.CustomerId;

public class LoyaltyPointDataMapper {

    public static LoyaltyPointEntity toEntity(LoyaltyPoint domain) {
        LoyaltyPointEntity entity = new LoyaltyPointEntity();
        entity.setId(LoyaltyId.toUUID(domain.getId()));
        entity.setCustomerId(domain.getCustomerId().getValue());
        entity.setPoints(domain.getPoints().getValue());
        entity.setLastUpdated(domain.getLastUpdated());
        return entity;
    }

    public static LoyaltyPoint toDomain(LoyaltyPointEntity entity) {
        return new LoyaltyPoint.Builder(new CustomerId(entity.getCustomerId()))
                .id(new LoyaltyId(entity.getId()))
                .points(new Point(entity.getPoints()))
                .lastUpdated(entity.getLastUpdated())
                .build();
    }

}