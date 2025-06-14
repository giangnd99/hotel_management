package edu_poly_notificationdomaincore.exception;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(Integer notificationId) {
        super("Notification not found with ID: " + notificationId);
    }
}