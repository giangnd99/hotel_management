package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.valueobject.DepositId;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.ReferenceId;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.Money;

import java.util.UUID;

public class Deposit extends BaseEntity<DepositId> {

    private Money amount;
    private ReferenceId referenceId;
    private double rate;
    private boolean isPaid;
    private DateCustom paidDate;
    private DateCustom dateOfDeposit;
    public static final double DEFAULT_RATE = 0.3;

    public void initDeposit(ReferenceId referenceId){
        setBookingId(referenceId);
        setId(new DepositId(UUID.randomUUID()));
        rate = DEFAULT_RATE;
        isPaid = false;
    }


    public void payDeposit(){
        if (!isPaid){
            isPaid = true;
            paidDate = DateCustom.now();
        }
        else{
            throw new IllegalStateException("Deposit is already paid");
        }
    }
    public void cancelDeposit(){
        if (isPaid){
            throw new IllegalStateException("Deposit is already paid");
        }
        else{
            isPaid = true;
            paidDate = DateCustom.now();
        }
        // Xu ly hoan tien
    }

    public void setBookingId(ReferenceId referenceId) {
        this.referenceId = referenceId;
    }

}
