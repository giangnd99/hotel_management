package edu.poly.notificationdomaincore.entity;

import edu.poly.notificationdomaincore.value_object.MessageContent;
import edu.poly.notificationdomaincore.value_object.NotificationMethod;
import edu.poly.notificationdomaincore.value_object.NotificationPriority;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity chính đại diện cho thông báo
 */
public class Notification {
    private Integer notificationId;
    private Integer userId;
    private MessageContent message;
    private NotificationMethod notificationMethod;
    private NotificationPriority priority;
    private LocalDateTime sentDate;
    private NotificationStatus status;
    private List<NotificationReceiver> receivers;

    // Constructor
    public Notification(Integer userId, MessageContent message,
                        NotificationMethod method, NotificationPriority priority) {
        this.userId = userId;
        this.message = message;
        this.notificationMethod = method;
        this.priority = priority;
        this.sentDate = LocalDateTime.now();
        this.status = NotificationStatus.PENDING;
    }

    // Getter và Setter
    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public MessageContent getMessage() {
        return message;
    }

    public NotificationMethod getNotificationMethod() {
        return notificationMethod;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public List<NotificationReceiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<NotificationReceiver> receivers) {
        this.receivers = receivers;
    }
}