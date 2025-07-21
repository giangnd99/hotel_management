package com.poly.customerdomain.model.exception;

import java.util.UUID;

public class UserExistException extends DomainException {
    public UserExistException(UUID userId) {
        super("UUID UserId: %s đã tồn tại.".formatted(userId));
    }
    @Override
    public String getErrorCode() {
        return "USER_ID_EXIST";
    }
}
