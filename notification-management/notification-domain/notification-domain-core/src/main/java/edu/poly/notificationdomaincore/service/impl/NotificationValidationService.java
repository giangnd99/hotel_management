package edu.poly.notificationdomaincore.service.impl;

import edu.poly.notificationdomaincore.entity.Notification;
import edu.poly.notificationdomaincore.exception.NotificationValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Service validate thông báo trước khi gửi
 */
public class NotificationValidationService {

    public void validate(Notification notification) throws NotificationValidationException {
        List<String> errors = new ArrayList<>();

        if (notification.getUserId() == null) {
            errors.add("User ID cannot be null");
        }

        if (notification.getMessage() == null || notification.getMessage().getContent().isEmpty()) {
            errors.add("Message content cannot be empty");
        }

        if (notification.getNotificationMethod() == null) {
            errors.add("Notification method cannot be null");
        }

        if (notification.getPriority() == null) {
            errors.add("Priority cannot be null");
        }

        if (!errors.isEmpty()) {
            throw new NotificationValidationException(errors);
        }
    }
}