package com.poly.booking.management.messaging.adapter.listener;

import com.poly.booking.management.domain.port.in.message.listener.BookingCheckInListener;
import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;
import com.poly.booking.management.domain.saga.checkin.BookingCheckInStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Booking Check-In Listener Implementation
 * <p>
 * CHỨC NĂNG:
 * - Xử lý booking check-in events từ Kafka messages
 * - Quản lý quy trình check-in trong hệ thống booking
 * - Tích hợp với Saga pattern để đảm bảo tính nhất quán dữ liệu
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông tin check-in từ notification service
 * - Thực hiện business logic check-in
 * - Cập nhật trạng thái booking và gửi thông báo xác nhận
 * <p>
 * BUSINESS RULES:
 * - Nhận tin nhắn từ notification service để trigger check-in
 * - Xử lý các trạng thái check-in: SUCCESS, FAILED, PENDING
 * - Cập nhật trạng thái booking tương ứng
 * - Gửi thông báo xác nhận check-in
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCheckInListenerImpl implements BookingCheckInListener {

    private final BookingCheckInStep bookingCheckInStep;

    /**
     * Xử lý check-in thành công
     * <p>
     * EVENT FLOW:
     * 1. Nhận thông tin check-in thành công từ notification service
     * 2. Validate và xử lý business logic
     * 3. Trigger check-in saga step
     * 4. Cập nhật trạng thái booking thành CHECKED_IN
     * <p>
     * BUSINESS LOGIC:
     * - Kiểm tra điều kiện check-in
     * - Cập nhật trạng thái booking
     * - Ghi log check-in thành công
     * <p>
     * SAGA INTEGRATION:
     * - Sử dụng BookingCheckInStep để xử lý saga
     * - Quản lý outbox messages
     * - Đảm bảo tính nhất quán dữ liệu
     *
     * @param notificationMessageResponse Thông tin check-in thành công từ notification service
     */
    @Override
    public void processBookingCheckIn(NotificationMessageResponse notificationMessageResponse) {
        log.info("Processing successful check-in for booking: {}", notificationMessageResponse.getBookingId());

        try {
            // Validate input data
            validateCheckInRequest(notificationMessageResponse);

            // Trigger check-in saga step
            bookingCheckInStep.process(notificationMessageResponse);

            log.info("Booking check-in processed successfully for booking: {}", 
                    notificationMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing booking check-in for booking: {}", 
                    notificationMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process booking check-in", e);
        }
    }

    /**
     * Xử lý check-in thất bại
     * <p>
     * EVENT FLOW:
     * 1. Nhận thông tin check-in thất bại từ notification service
     * 2. Validate và xử lý business logic
     * 3. Trigger failed check-in saga step
     * 4. Ghi log thất bại và cập nhật trạng thái
     * <p>
     * BUSINESS LOGIC:
     * - Ghi log check-in thất bại
     * - Cập nhật trạng thái booking nếu cần
     * - Gửi thông báo lỗi check-in
     *
     * @param notificationMessageResponse Thông tin check-in thất bại từ notification service
     */
    @Override
    public void processBookingCheckInFailed(NotificationMessageResponse notificationMessageResponse) {
        log.info("Processing failed check-in for booking: {}", notificationMessageResponse.getBookingId());

        try {
            // Validate input data
            validateCheckInRequest(notificationMessageResponse);

            // Trigger failed check-in saga step
            bookingCheckInStep.processFailed(notificationMessageResponse);

            log.info("Failed check-in processed successfully for booking: {}", 
                    notificationMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing failed check-in for booking: {}", 
                    notificationMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process failed check-in", e);
        }
    }

    /**
     * Xử lý check-in đang chờ
     * <p>
     * EVENT FLOW:
     * 1. Nhận thông tin check-in đang chờ từ notification service
     * 2. Validate và xử lý business logic
     * 3. Trigger pending check-in saga step
     * 4. Cập nhật trạng thái booking thành PENDING_CHECKIN
     * <p>
     * BUSINESS LOGIC:
     * - Ghi log check-in đang chờ
     * - Cập nhật trạng thái booking thành PENDING_CHECKIN
     * - Gửi thông báo chờ xử lý
     *
     * @param notificationMessageResponse Thông tin check-in đang chờ từ notification service
     */
    @Override
    public void processBookingCheckInPending(NotificationMessageResponse notificationMessageResponse) {
        log.info("Processing pending check-in for booking: {}", notificationMessageResponse.getBookingId());

        try {
            // Validate input data
            validateCheckInRequest(notificationMessageResponse);

            // Trigger pending check-in saga step
            bookingCheckInStep.processPending(notificationMessageResponse);

            log.info("Pending check-in processed successfully for booking: {}", 
                    notificationMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing pending check-in for booking: {}", 
                    notificationMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process pending check-in", e);
        }
    }

    /**
     * Validate check-in request
     * <p>
     * CHECKS:
     * - NotificationMessageResponse không được null
     * - Booking ID và Customer ID phải hợp lệ
     * - Notification status phải hợp lệ
     *
     * @param notificationMessageResponse NotificationMessageResponse cần validate
     * @throws IllegalArgumentException nếu validation fail
     */
    private void validateCheckInRequest(NotificationMessageResponse notificationMessageResponse) {
        if (notificationMessageResponse == null) {
            throw new IllegalArgumentException("Notification message response cannot be null");
        }

        if (notificationMessageResponse.getBookingId() == null || 
            notificationMessageResponse.getBookingId().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        if (notificationMessageResponse.getCustomerId() == null || 
            notificationMessageResponse.getCustomerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }

        if (notificationMessageResponse.getNotificationStatus() == null) {
            throw new IllegalArgumentException("Notification status cannot be null");
        }

        log.debug("Check-in request validation passed for booking: {}", 
                notificationMessageResponse.getBookingId());
    }
}
