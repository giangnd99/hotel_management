package com.poly.paymentdomain.model.exception;

public class InvalidValueException extends DomainException {
    public InvalidValueException(String message) {
        super("ID %s đang trống.".formatted(message));
    }
    public InvalidValueException(Object value, String field, String context) {
        super("Giá trị '%s' không hợp lệ cho trường '%s' trong '%s'.".formatted(value, field, context));
    }
}
