package com.poly.customerdomain.model.exception;

public class InvalidPointAmountException extends DomainException{
    public InvalidPointAmountException() {
        super("Điểm đổi không hợp lệ.");
    }
    @Override
    public String getErrorCode() {
        return "INVALID_POINT_AMOUNT";
    }
}
