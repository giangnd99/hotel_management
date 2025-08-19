package com.poly.promotion.domain.core.exception;

import com.poly.domain.exception.DomainException;

/**
 * <h2>VoucherDomainException</h2>
 * 
 * <p>This exception is thrown when an error occurs in the voucher domain.
 * It extends the DomainException class to provide a consistent exception
 * hierarchy for voucher-related errors.</p>
 */
public class VoucherDomainException extends DomainException {
    /**
     * Constructs a new VoucherDomainException with the specified message.
     * 
     * @param message the detail message (which is saved for later retrieval
     *                by the getMessage() method)
     */
    public VoucherDomainException(String message) {
        super(message);
    }

    /**
     * Constructs a new VoucherDomainException with the specified message and cause.
     * 
     * @param message the detail message (which is saved for later retrieval
     *                by the getMessage() method)
     * @param cause the cause (which is saved for later retrieval by the getCause() method).
     *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public VoucherDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
