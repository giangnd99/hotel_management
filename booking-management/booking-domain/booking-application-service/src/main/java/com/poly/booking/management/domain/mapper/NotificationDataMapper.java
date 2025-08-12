package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.event.BookingConfirmedEvent;
import com.poly.booking.management.domain.outbox.payload.NotifiEventPayload;
import org.springframework.stereotype.Component;

@Component
public class NotificationDataMapper {


    /**
     * Chuyển đổi BookingPaidEvent thành BookingReservedEventPayload
     * <p>
     * Mục đích: Tạo payload cho Kafka message để thông báo room service về việc đặt phòng đã thanh toán
     * Sử dụng: Gửi message đến room service để cập nhật trạng thái phòng
     *
     * @param domainEvent BookingPaidEvent từ domain
     * @return BookingReservedEventPayload cho Kafka message
     */
    public NotifiEventPayload bookingRoomReservedEventToBookingNotifiEventPayload(BookingConfirmedEvent domainEvent) {
        return NotifiEventPayload.builder()
                .bookingId(domainEvent.getBooking().getId().getValue())
                .customerId(domainEvent.getBooking().getCustomerId().getValue())
                .createdAt(domainEvent.getCreatedAt().getValue())
                .checkInTime(domainEvent.getBooking().getCheckInDate().getValue())
                .bookingStatus(domainEvent.getBooking().getStatus())
                .build();
    }
}
