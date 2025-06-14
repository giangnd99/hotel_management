package edu.poly.notificationdomaincore.entity;

/**
 * Entity đại diện cho người nhận thông báo
 */
public class NotificationReceiver {
    private Integer notificationId;
    private Integer userId;
    private String receiverType; // receiver/cc/bcc
    private String additionalEmail;

    public NotificationReceiver(Integer notificationId, Integer userId,
                                String receiverType, String additionalEmail) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.receiverType = receiverType;
        this.additionalEmail = additionalEmail;
    }

    // Getter và Setter
    public Integer getNotificationId() {
        return notificationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public String getAdditionalEmail() {
        return additionalEmail;
    }
}