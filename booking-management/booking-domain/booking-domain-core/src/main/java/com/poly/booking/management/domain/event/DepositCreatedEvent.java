package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Deposit;
import com.poly.domain.valueobject.DateCustom;

public class DepositCreatedEvent extends DepositEvent{
    public DepositCreatedEvent(Deposit deposit, DateCustom createdAt) {
        super(deposit, createdAt);
    }
}
