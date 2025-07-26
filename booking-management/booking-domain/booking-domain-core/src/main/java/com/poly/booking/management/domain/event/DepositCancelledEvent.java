package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Deposit;
import com.poly.domain.valueobject.DateCustom;

public class DepositCancelledEvent extends DepositEvent {
    public DepositCancelledEvent(Deposit deposit, DateCustom createdAt) {
        super(deposit, createdAt);
    }
}
