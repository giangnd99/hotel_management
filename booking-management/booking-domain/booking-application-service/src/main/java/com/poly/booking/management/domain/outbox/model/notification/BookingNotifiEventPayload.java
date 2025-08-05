package com.poly.booking.management.domain.outbox.model.notification;

import com.poly.domain.valueobject.EBookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * BookingNotifiEventPayload - Event Payload for Notification Processing
 * 
 * CHỨC NĂNG:
 * - Chứa dữ liệu event cho notification processing
 * - Được sử dụng trong outbox pattern để serialize/deserialize notification data
 * - Track thông tin check-in và notification status
 * 
 * CÁC TRƯỜNG DỮ LIỆU:
 * - id: Unique identifier cho event
 * - bookingId: Booking identifier
 * - customerId: Customer identifier
 * - qrCode: QR code được quét
 * - checkInTime: Thời gian check-in
 * - notificationStatus: Trạng thái notification
 * - bookingStatus: Trạng thái booking
 * - createdAt: Thời gian tạo event
 */
@Getter
@Builder
@AllArgsConstructor
public class BookingNotifiEventPayload {
    private UUID id;
    private UUID bookingId;
    private UUID customerId;
    private String qrCode;
    private LocalDateTime checkInTime;
    private NotificationStatus notificationStatus;
    private EBookingStatus bookingStatus;
    private LocalDateTime createdAt;
    
    /**
     * NotificationStatus - Enum định nghĩa trạng thái notification
     */
    public enum NotificationStatus {
        SUCCESS, FAILED, PENDING, CANCELLED
    }
} 