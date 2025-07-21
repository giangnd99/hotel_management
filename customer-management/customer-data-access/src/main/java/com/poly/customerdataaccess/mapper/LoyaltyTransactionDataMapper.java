package com.poly.customerdataaccess.mapper;

import com.poly.customerdataaccess.entity.LoyaltyTransactionEntity;
import com.poly.customerdomain.model.entity.LoyaltyTransaction;
import com.poly.customerdomain.model.entity.valueobject.Description;
import com.poly.customerdomain.model.entity.valueobject.LoyaltyId;
import com.poly.customerdomain.model.entity.valueobject.PointChanged;
import com.poly.customerdomain.model.entity.valueobject.TransactionId;

public class LoyaltyTransactionDataMapper {

    public static LoyaltyTransactionEntity toEntity(LoyaltyTransaction domain) {
        LoyaltyTransactionEntity entity = new LoyaltyTransactionEntity();
        entity.setId(TransactionId.to(domain.getId()));
        entity.setLoyaltyPointId(LoyaltyId.toUUID(domain.getLoyaltyId()));
        entity.setPointChange(PointChanged.to(domain.getPointChanged()));
        entity.setTransactionType(domain.getTransactionType());
        entity.setTransactionDate(domain.getTransactionDate());
        entity.setDescription(domain.getDescription().toString());
        return entity;
    }

    public static LoyaltyTransaction toDomain(LoyaltyTransactionEntity entity) {
        return new LoyaltyTransaction.Builder(
                LoyaltyId.fromUUID(entity.getLoyaltyPointId()),
                PointChanged.from(entity.getPointChange()),
                entity.getTransactionType(),
                entity.getTransactionDate(),
                Description.from(entity.getDescription())
        )
                .id(TransactionId.from(entity.getId()))
                .build();
    }


}
