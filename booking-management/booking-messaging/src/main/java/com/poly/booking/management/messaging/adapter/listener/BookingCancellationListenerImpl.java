package com.poly.booking.management.messaging.adapter.listener;

import com.poly.booking.management.domain.port.in.message.listener.BookingCancellationListener;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
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

    /**
     * Xử lý booking cancellation event
     * <p>
     * EVENT FLOW:
     * 1. Nhận thông tin hủy booking từ room service
     * 2. Validate và xử lý business logic
     * 3. Trigger cancellation saga step
     * 4. Xử lý hoàn tiền nếu đủ điều kiện
     * <p>
     * BUSINESS LOGIC:
     * - Kiểm tra điều kiện hủy booking
     * - Xác định có hoàn tiền hay không dựa trên thời gian
     * - Cập nhật trạng thái booking
     * <p>
     * SAGA INTEGRATION:
     * - Sử dụng BookingCancellationStep để xử lý saga
     * - Quản lý outbox messages
     * - Xử lý rollback khi có lỗi
     *
     * @param roomMessageResponse Thông tin hủy booking từ room service
     */
    @Override
    public void processBookingCancellation(RoomMessageResponse roomMessageResponse) {
        log.info("Processing booking cancellation for booking: {}", roomMessageResponse.getBookingId());

        try {
            // Validate input data
            validateCancellationRequest(roomMessageResponse);

            // Trigger cancellation saga step
            bookingCancellationStep.process(roomMessageResponse);

            log.info("Booking cancellation processed successfully for booking: {}", 
                    roomMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing booking cancellation for booking: {}", 
                    roomMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process booking cancellation", e);
        }
    }

    /**
     * Xử lý booking cancellation rollback
     * <p>
     * ROLLBACK FLOW:
     * 1. Nhận thông tin rollback từ room service
     * 2. Thực hiện rollback business logic
     * 3. Cập nhật trạng thái và outbox messages
     * <p>
     * COMPENSATION LOGIC:
     * - Revert booking status về trạng thái trước đó
     * - Cập nhật outbox messages
     * - Đảm bảo tính nhất quán dữ liệu
     *
     * @param roomMessageResponse Thông tin rollback từ room service
     */
    @Override
    public void processBookingCancellationRollback(RoomMessageResponse roomMessageResponse) {
        log.info("Processing booking cancellation rollback for booking: {}", 
                roomMessageResponse.getBookingId());

        try {
            // Validate input data
            validateCancellationRequest(roomMessageResponse);

            // Trigger rollback saga step
            bookingCancellationStep.rollback(roomMessageResponse);

            log.info("Booking cancellation rollback processed successfully for booking: {}", 
                    roomMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing booking cancellation rollback for booking: {}", 
                    roomMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process booking cancellation rollback", e);
        }
    }

    /**
     * Validate cancellation request
     * <p>
     * CHECKS:
     * - RoomMessageResponse không được null
     * - Booking ID và Saga ID phải hợp lệ
     * - Room response status phải hợp lệ
     *
     * @param roomMessageResponse RoomMessageResponse cần validate
     * @throws IllegalArgumentException nếu validation fail
     */
    private void validateCancellationRequest(RoomMessageResponse roomMessageResponse) {
        if (roomMessageResponse == null) {
            throw new IllegalArgumentException("Room message response cannot be null");
        }

        if (roomMessageResponse.getBookingId() == null || 
            roomMessageResponse.getBookingId().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        if (roomMessageResponse.getSagaId() == null || 
            roomMessageResponse.getSagaId().trim().isEmpty()) {
            throw new IllegalArgumentException("Saga ID cannot be null or empty");
        }

        log.debug("Cancellation request validation passed for booking: {}", 
                roomMessageResponse.getBookingId());
    }
}
