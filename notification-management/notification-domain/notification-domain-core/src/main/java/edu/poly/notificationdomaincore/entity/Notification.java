package edu.poly.notificationdomaincore.entity;

import edu.poly.notificationdomaincore.value_object.NotificationMethod;
import edu.poly.notificationdomaincore.value_object.NotificationPriority;
import edu.poly.notificationdomaincore.value_object.NotificationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity đại diện cho một thông báo trong hệ thống
 */
public class Notification {
    private Integer notificationId;
    private UUID userId;
    private String message;
    private NotificationMethod method;
    private NotificationPriority priority;
    private LocalDateTime sentDate;
    private NotificationStatus status;

    // Constructors, getters, setters
    public Notification(Integer notificationId, UUID userId, String message,
                        NotificationMethod method, NotificationPriority priority) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.method = method;
        this.priority = priority;
        this.sentDate = LocalDateTime.now();
        this.status = NotificationStatus.PENDING;
    }

    // Business methods
    public void markAsSent() {
        this.status = NotificationStatus.SENT;
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }

    // Getters and setters
    // ...
}