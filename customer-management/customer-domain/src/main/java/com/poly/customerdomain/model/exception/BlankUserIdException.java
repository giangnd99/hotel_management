package com.poly.customerdomain.model.exception;

public class BlankUserIdException extends DomainException {
    public BlankUserIdException() {
        super("User Id đang trống.");
    }

    @Override
    public String getErrorCode() {
        return "APPLICATION_SERVICE_USERID_BLANK_USER_ID";
    }
}
