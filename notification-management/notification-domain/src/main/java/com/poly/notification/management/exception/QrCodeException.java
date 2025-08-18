package com.poly.notification.management.exception;

public class QrCodeException extends RuntimeException {
    
    public QrCodeException(String message) {
        super(message);
    }
    
    public QrCodeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public QrCodeException(Throwable cause) {
        super(cause);
    }
}
