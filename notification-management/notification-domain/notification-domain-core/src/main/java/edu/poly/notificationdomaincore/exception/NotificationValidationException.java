package edu.poly.notificationdomaincore.exception;

import java.util.List;

/**
 * Exception khi validate thông báo không hợp lệ
 */
public class NotificationValidationException extends NotificationException {
    private final List<String> validationErrors;

    public NotificationValidationException(List<String> validationErrors) {
        super("Notification validation failed");
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}