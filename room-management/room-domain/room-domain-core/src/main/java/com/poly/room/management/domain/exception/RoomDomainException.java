package com.poly.room.management.domain.exception;

import com.poly.domain.exception.DomainException;

public class RoomDomainException extends DomainException {
    public RoomDomainException(String message) {
        super(message);
    }

    public RoomDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
