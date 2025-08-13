package com.poly.paymentdomain.model.exception;

public class PaymentNotFoundException extends DomainException {
    public PaymentNotFoundException() {
        super("Thanh toán không tồn tại.");
    }

    public String getErrorCode() {
        return "PAYMENT_NOT_FOUND";
    }
}
