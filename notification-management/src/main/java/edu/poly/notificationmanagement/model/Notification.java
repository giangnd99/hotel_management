
package edu.poly.notificationmanagement.model;

import java.util.Date;

public class Notification {
    private int notificationId;
    private int userId;
    private String notificationMethod;
    private String message;
    private String priority;
    private Date sentDate;
    private String status;

    // Constructors, Getters and Setters
    public Notification(int notificationId, int userId, String notificationMethod, String message, String priority, Date sentDate, String status) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.notificationMethod = notificationMethod;
        this.message = message;
        this.priority = priority;
        this.sentDate = sentDate;
        this.status = status;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNotificationMethod() {
        return notificationMethod;
    }

    public void setNotificationMethod(String notificationMethod) {
        this.notificationMethod = notificationMethod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
