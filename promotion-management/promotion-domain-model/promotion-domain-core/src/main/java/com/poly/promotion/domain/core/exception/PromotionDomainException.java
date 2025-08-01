package com.poly.promotion.domain.core.exception;

import com.poly.domain.exception.DomainException;

public class PromotionDomainException extends DomainException {
    public PromotionDomainException(String message) {
        super(message);
    }

    public PromotionDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
