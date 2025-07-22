package com.poly.booking.management.domain.event;

public class DepositCancelledEvent extends DepositEvent {
    public DepositCancelledEvent(Object source, com.poly.booking.management.domain.entity.Deposit deposit, com.poly.domain.valueobject.DateCustom createdAt) {
        super(deposit, createdAt);
    }
}
