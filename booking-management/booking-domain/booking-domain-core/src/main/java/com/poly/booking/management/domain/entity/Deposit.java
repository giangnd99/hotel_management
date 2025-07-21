package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.valueobject.DepositId;
import com.poly.booking.management.domain.valueobject.DepositStatus;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.*;

import java.util.List;
import java.util.UUID;

public class Deposit extends BaseEntity<DepositId> {

    private Money amount;
    private BookingId bookingId;
    private double rate;
    private CustomerId customerId;
    private DateCustom dateOfDeposit;
    private DepositStatus depositStatus;
    public static final double DEFAULT_RATE = 0.3;

    public void initDeposit(BookingId bookingId) {
        setBookingId(bookingId);
        setId(new DepositId(UUID.randomUUID()));
        rate = DEFAULT_RATE;

    }

    public void paidDeposit() {
        if (!depositStatus.equals(DepositStatus.APPROVED)) {
            throw new BookingDomainException("Payment is not approved");
        }
        dateOfDeposit = DateCustom.now();
        depositStatus = DepositStatus.APPROVED;
    }

    public void cancelDeposit(List<String> failureMessages) {
        if (depositStatus.equals(DepositStatus.APPROVED)) {
            throw new BookingDomainException("Deposit is approved");
        }
        depositStatus = DepositStatus.CANCELLED;
        failureMessages.add("Deposit is cancelled with reason: payment is not approved");
    }

    public void processRefundAfterDeposit() {

    }

    public void checkRegularityForRefundDeposit() {
        if( dateOfDeposit.getDay() - DateCustom.now().getDay() > 3){
            throw new IllegalStateException("Refund is not regular");
        }
    }


    public void setBookingId(BookingId bookingId) {
        this.bookingId = bookingId;
    }

}
