package com.poly.customerdomain.model.exception;

public class NotEnoughPointException extends DomainException{
    public NotEnoughPointException() {
        super("Điểm khách hàng không đủ.");
    }

    @Override
    public String getErrorCode() {
        return "NOT_ENOUGH_POINT";
    }
}
