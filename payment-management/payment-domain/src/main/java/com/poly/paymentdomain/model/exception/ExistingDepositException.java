package com.poly.paymentdomain.model.exception;

public class ExistingDepositException extends DomainException {
    public ExistingDepositException() {
        super("Đặt cọc đã có tại booking này rồi.");
    }

    public String getErrorCode() {
        return "";
    }
}
