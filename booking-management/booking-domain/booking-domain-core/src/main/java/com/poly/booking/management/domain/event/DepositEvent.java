package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Deposit;
import com.poly.domain.event.DomainEvent;
import com.poly.domain.valueobject.DateCustom;

public abstract class DepositEvent implements DomainEvent<Deposit> {

    private final Deposit deposit;
    private final DateCustom createdAt;

    public DepositEvent(Deposit deposit, DateCustom createdAt) {
        this.deposit = deposit;
        this.createdAt = createdAt;
    }

    public Deposit getDeposit() {
        return deposit;
    }
    public DateCustom getCreatedAt() {
        return createdAt;
    }
}
