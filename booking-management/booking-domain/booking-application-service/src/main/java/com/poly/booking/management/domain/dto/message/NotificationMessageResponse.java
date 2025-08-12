package com.poly.booking.management.domain.dto.message;

import com.poly.domain.valueobject.EBookingStatus;
import com.poly.domain.valueobject.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

/**
 * NotificationMessageResponse - DTO for QR Check-in Notification Processing
 * <p>
 * CHỨC NĂNG:
 * - Chứa thông tin response từ notification service sau khi xử lý QR check-in
 * - Được sử dụng trong Saga pattern để xử lý check-in flow
 * <p>
 * CÁC TRƯỜNG DỮ LIỆU:
 * - id: Unique identifier cho notification message
 * - sagaId: Saga identifier để track transaction
 * - bookingId: Booking identifier được check-in
 * - customerId: Customer identifier thực hiện check-in
 * - qrCode: QR code được quét
 * - checkInTime: Thời gian check-in thực tế
 * - notificationStatus: Trạng thái xử lý notification
 * - bookingStatus: Trạng thái booking sau check-in
 * - failureMessages: Danh sách lỗi nếu có
 */
@Getter
@Builder
@AllArgsConstructor
public class NotificationMessageResponse {
    private String id;
    private String sagaId;
    private String bookingId;
    private String customerId;
    private String qrCode;
    private Instant checkInTime;
    private NotificationStatus notificationStatus;
    private EBookingStatus bookingStatus;
    private List<String> failureMessages;
}
