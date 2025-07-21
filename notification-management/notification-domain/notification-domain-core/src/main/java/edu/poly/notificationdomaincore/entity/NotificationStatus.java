package edu.poly.notificationdomaincore.entity;

/**
 * Enum trạng thái của thông báo
 */
public enum NotificationStatus {
    PENDING,    // Đang chờ gửi
    SENT,       // Đã gửi thành công
    FAILED,     // Gửi thất bại
    DELIVERED,  // Đã gửi đến người nhận (cho email/SMS)
    READ        // Đã đọc (cho in-app notification)
}