package com.poly.paymentdomain.model.entity;

import com.poly.paymentdomain.model.entity.valueobject.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Payment {
//    private PaymentId id;
    private StaffId  staffId; // Người thực hiện xác nhận thanh toán của khách hàng, SYSTEM - CK, ID nhân viên - tiền mặt
    private PaymentStatus paymentStatus; // Trạng thái thanh toán
    private Money amount; // Tổng số tiền thanh toán
    private PaymentMethod method; // CASH, PAYOS
    private LocalDateTime paidAt; // Thời gian trả
    private PaymentReference referenceCode; // mã QR hoặc mã giao dịch

    private Payment(Builder builder) {
//        id = builder.id;
        staffId = builder.staffId;
        paymentStatus = builder.paymentStatus;
        amount = builder.amount;
        method = builder.method;
        paidAt = builder.paidAt;
        referenceCode = builder.referenceCode;
    }

    public Money amount() {
        return amount;
    }

    public static final class Builder {
//        private PaymentId id;
        private StaffId staffId;
        private PaymentStatus paymentStatus;
        private Money amount;
        private PaymentMethod method;
        private LocalDateTime paidAt;
        private PaymentReference referenceCode;

        public Builder() {
        }

//        public Builder id(PaymentId val) {
//            id = val;
//            return this;
//        }

        public Builder staffId(StaffId val) {
            staffId = val;
            return this;
        }

        public Builder paymentStatus(PaymentStatus val) {
            paymentStatus = val;
            return this;
        }

        public Builder amount(Money val) {
            amount = val;
            return this;
        }

        public Builder method(PaymentMethod val) {
            method = val;
            return this;
        }

        public Builder paidAt(LocalDateTime val) {
            paidAt = val;
            return this;
        }

        public Builder referenceCode(PaymentReference val) {
            referenceCode = val;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}