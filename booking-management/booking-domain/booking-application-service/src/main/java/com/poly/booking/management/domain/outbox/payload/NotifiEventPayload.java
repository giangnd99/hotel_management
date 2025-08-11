package com.poly.booking.management.domain.outbox.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.poly.domain.valueobject.EBookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * BookingNotifiEventPayload - Event Payload for Notification Processing
 * <p>
 * CHỨC NĂNG:
 * - Chứa dữ liệu event cho notification processing
 * - Được sử dụng trong outbox pattern để serialize/deserialize notification data
 * - Track thông tin check-in và notification status
 * <p>
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
public class NotifiEventPayload {
    @JsonProperty
    private UUID id;
    @JsonProperty
    private UUID bookingId;
    @JsonProperty
    private UUID customerId;
    @JsonProperty
    private LocalDateTime checkInTime;
    @JsonProperty
    private String notificationStatus;
    @JsonProperty
    private EBookingStatus bookingStatus;
    @JsonProperty
    private LocalDateTime createdAt;
} 