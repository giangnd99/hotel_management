package edu.poly.notificationdomaincore.entity;

public class NotificationReceiver {
    private Integer notificationId;
    private Integer userId;
    private String receiverType; // receiver/cc/bcc
    private String additionalEmail;

    // Constructor, getters, setters
    public NotificationReceiver(Integer notificationId, Integer userId,
                                String receiverType, String additionalEmail) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.receiverType = receiverType;
        this.additionalEmail = additionalEmail;
    }

    // Getters and setters...
}