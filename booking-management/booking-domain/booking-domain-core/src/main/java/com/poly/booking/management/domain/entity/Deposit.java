package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.valueobject.DepositId;
import com.poly.booking.management.domain.valueobject.DepositStatus;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Deposit extends BaseEntity<DepositId> {

    private Money amount;
    private BookingId bookingId;
    private double rate;
    private CustomerId customerId;
    private DateCustom dateOfDeposit;
    private DepositStatus depositStatus;
    private String refundReason;
    public static final double DEFAULT_RATE = 0.3;

    //khởi taọ khi khách hàng nhấp vào email được gửi qua booking
    public void initDeposit(BookingId bookingId) {
        setBookingId(bookingId);
        setId(new DepositId(UUID.randomUUID()));
        rate = DEFAULT_RATE;
        depositStatus = DepositStatus.PENDING;
    }

    //Tạo yeu cầu thanh toán cho payment trên kafka
    public void payDeposit() {
        if (!depositStatus.equals(DepositStatus.PENDING)) {
            throw new BookingDomainException("Payment is not pending");
        }
        dateOfDeposit = DateCustom.now();
    }

    // cancel trong trường hợp thanh toán thất bại
    public void cancelDeposit(List<String> failureMessages) {
        if (depositStatus.equals(DepositStatus.APPROVED)) {
            throw new BookingDomainException("Deposit is approved");
        }
        depositStatus = DepositStatus.CANCELLED;
        failureMessages.add("Deposit is cancelled with reason: payment is not approved");
    }

    //Tao yeu cau thanh toan de hoàn tiền cho payment service tren kafka
    public void processRefundAfterDeposit(String refundReason) {
        if (!depositStatus.equals(DepositStatus.APPROVED)) {
            throw new BookingDomainException("Deposit is not approved");
        }
        checkRegularityForRefundDeposit();
        this.refundReason = refundReason;
        depositStatus = DepositStatus.REFUNDING;
    }

    public void refundDeposit() {
        if (!depositStatus.equals(DepositStatus.REFUNDING)) {
            throw new BookingDomainException("Refund is not processing");
        }
        depositStatus = DepositStatus.REFUNDED;
    }

    public void checkRegularityForRefundDeposit() {
        if (dateOfDeposit.getDay() - DateCustom.now().getDay() > 3) {
            throw new IllegalStateException("Refund is not regular");
        }
    }

    public void setAmount(Money totalRoomCost) {
        this.amount = totalRoomCost.multiply(new BigDecimal(rate));
    }

    public void setBookingId(BookingId bookingId) {
        this.bookingId = bookingId;
    }

    public void setCustomerId(CustomerId customerId) {
        this.customerId = customerId;
    }
}
