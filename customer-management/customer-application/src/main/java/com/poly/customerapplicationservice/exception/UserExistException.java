package com.poly.customerapplicationservice.exception;

import java.util.UUID;

public class UserExistException extends ApplicationServiceException {
    public UserExistException(UUID userId) {
        super("UUID UserId: %s đã tồn tại.".formatted(userId));
    }

    @Override
    public String getErrorCode() {
        return "USER_ID_EXIST";
    }
}
