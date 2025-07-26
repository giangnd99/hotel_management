package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Deposit;
import com.poly.domain.valueobject.DateCustom;

public class DepositPaidEvent extends DepositEvent{
    public DepositPaidEvent(Deposit deposit, DateCustom createdAt) {
        super(deposit, createdAt);
    }
}
