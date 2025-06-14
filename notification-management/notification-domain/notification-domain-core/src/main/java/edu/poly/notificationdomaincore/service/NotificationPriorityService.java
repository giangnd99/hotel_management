package edu.poly.notificationdomaincore.service;



import edu.poly.notificationdomaincore.value_object.NotificationPriority;

import static edu.poly.notificationdomaincore.value_object.NotificationPriority.*;

/**
 * Service xử lý logic ưu tiên của thông báo
 */
public class NotificationPriorityService {

    /**
     * Xác định thời gian chờ tối đa trước khi gửi dựa trên mức độ ưu tiên
     * @param priority Mức độ ưu tiên
     * @return Thời gian chờ tối đa tính bằng phút
     */
    public int getMaxDelayMinutes(NotificationPriority priority) {
        switch (priority) {
            case HIGH: return 1;    // Ưu tiên cao - gửi ngay
            case MEDIUM: return 15; // Ưu tiên trung bình - gửi trong vòng 15 phút
            case LOW: return 60;    // Ưu tiên thấp - gửi trong vòng 1 giờ
            default: return 30;     // Mặc định 30 phút
        }
    }

    /**
     * Kiểm tra xem thông báo có nên được gửi ngay không
     * @param priority Mức độ ưu tiên
     * @return true nếu nên gửi ngay, false nếu có thể chờ
     */
    public boolean shouldSendImmediately(NotificationPriority priority) {
        return priority == NotificationPriority.HIGH;
    }
}