package com.poly.customerapplicationservice.exception;

public class BlankUserIdException extends ApplicationServiceException {
    public BlankUserIdException() {
        super("User Id đang trống.");
    }

    @Override
    public String getErrorCode() {
        return "APPLICATION_SERVICE_USERID_BLANK_USER_ID";
    }
}
