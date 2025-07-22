package com.poly.booking.management.domain.event;

public class DepositCreatedEvent extends DepositEvent{
    public DepositCreatedEvent(Object source, com.poly.booking.management.domain.entity.Deposit deposit, com.poly.domain.valueobject.DateCustom createdAt) {
        super(deposit, createdAt);
    }
}
