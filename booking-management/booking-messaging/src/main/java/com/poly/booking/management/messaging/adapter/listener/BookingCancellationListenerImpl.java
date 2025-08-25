package com.poly.booking.management.messaging.adapter.listener;

import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;
import com.poly.booking.management.domain.port.in.message.listener.BookingCancellationListener;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.port.out.client.RoomClient;
import com.poly.booking.management.domain.saga.cancellation.BookingCancellationStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Booking Cancellation Listener Implementation
 * <p>
 * CHỨC NĂNG:
 * - Xử lý booking cancellation events từ Kafka messages
 * - Quản lý quy trình hủy booking trong hệ thống booking
 * - Tích hợp với Saga pattern để đảm bảo tính nhất quán dữ liệu
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông tin hủy booking từ room management service
 * - Thực hiện business logic hủy booking
 * - Xử lý hoàn tiền theo chính sách của khách sạn
 * <p>
 * BUSINESS RULES:
 * - Nhận tin nhắn từ room message response để trigger hủy booking
 * - Kiểm tra thời gian: nếu cách ngày check-in 1 ngày → cho phép hủy nhưng không hoàn tiền
 * - Hoàn tiền: Gửi tin nhắn đến payment service với topic hoàn tiền + số tiền đã cọc
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCancellationListenerImpl implements BookingCancellationListener {

    private final BookingCancellationStep bookingCancellationStep;
    private final RoomClient roomClient;

    @Override
    public void processBookingCancellation(NotificationMessageResponse notificationMessageResponse) {
        log.info("Processing booking cancellation for booking: {}", notificationMessageResponse.getBookingId());

        try {
            validateCancellationRequest(notificationMessageResponse);

            bookingCancellationStep.process(notificationMessageResponse);

            log.info("Booking cancellation processed successfully for booking: {}",
                    notificationMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing booking cancellation for booking: {}",
                    notificationMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process booking cancellation", e);
        }
    }


    private void validateCancellationRequest(NotificationMessageResponse roomMessageResponse) {
        if (roomMessageResponse == null) {
            throw new IllegalArgumentException("Room message response cannot be null");
        }

        if (roomMessageResponse.getBookingId() == null || 
            roomMessageResponse.getBookingId().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        log.debug("Cancellation request validation passed for booking: {}", 
                roomMessageResponse.getBookingId());
    }
}
