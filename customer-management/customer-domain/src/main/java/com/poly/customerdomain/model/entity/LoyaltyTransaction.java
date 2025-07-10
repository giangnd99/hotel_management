package com.poly.customerdomain.model.entity;

import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.domain.valueobject.CustomerId;

public class LoyaltyTransaction {

    private TransactionId id;
    private CustomerId customerId;
    private LoyaltyId loyaltyId;
    private PointChanged pointChanged;
    private TransactionType transactionType;
    private TransactionDate transactionDate;
    private Description description;

}
