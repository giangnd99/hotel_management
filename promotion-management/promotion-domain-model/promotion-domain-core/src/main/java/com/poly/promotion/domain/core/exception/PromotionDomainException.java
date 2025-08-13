package com.poly.promotion.domain.core.exception;

import com.poly.domain.exception.DomainException;

/**
 * <h2>PromotionDomainException</h2>
 * 
 * <p>This exception is thrown when an error occurs in the promotion domain.
 * It extends the DomainException class to provide a consistent exception
 * hierarchy for promotion-related errors.</p>
 */
public class PromotionDomainException extends DomainException {
    /**
     * Constructs a new PromotionDomainException with the specified message.
     * 
     * @param message the detail message (which is saved for later retrieval
     *                by the getMessage() method)
     */
    public PromotionDomainException(String message) {
        super(message);
    }

    /**
     * Constructs a new PromotionDomainException with the specified message and cause.
     * 
     * @param message the detail message (which is saved for later retrieval
     *                by the getMessage() method)
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public PromotionDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
