package edu_poly_notificationdomaincore.value_object;

/**
 * Enum các phương thức gửi thông báo
 */
public enum NotificationMethod {
    EMAIL,      // Gửi qua email
    SMS,        // Gửi qua SMS
    IN_APP,     // Thông báo trong ứng dụng
    PUSH        // Push notification đến thiết bị di động
}