package edu.poly.notificationdomaincore.exception;

import edu.poly.notificationdomaincore.exception.NotificationException;

/**
 * Exception khi không tìm thấy thông báo
 */
public class NotificationNotFoundException extends NotificationException {
    public NotificationNotFoundException(Integer notificationId) {
        super("Notification with ID " + notificationId + " not found");
    }
}