package com.poly.paymentdomain.model.exception;

public class AlreadyConfirmedPaymentException extends DomainException {
    public AlreadyConfirmedPaymentException() {
        super("Giao dịch đã được thực hiện.");
    }

}
