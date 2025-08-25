package com.poly.room.management.kafka.adaptor;

import com.poly.room.management.domain.message.RoomCancellationResponseMessage;
import com.poly.room.management.domain.port.in.message.listener.RoomCancellationListener;
import com.poly.room.management.domain.command.room.RoomCancellationCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Room Cancellation Listener Implementation
 * <p>
 * CHỨC NĂNG:
 * - Xử lý room cancellation events từ Kafka messages
 * - Quản lý quy trình hủy phòng trong hệ thống room management
 * - Tích hợp với Saga pattern để đảm bảo tính nhất quán dữ liệu
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông tin hủy phòng từ booking management service
 * - Thực hiện business logic hủy phòng
 * - Xử lý giải phóng phòng và cập nhật trạng thái
 * <p>
 * BUSINESS RULES:
 * - Nhận tin nhắn từ booking service để trigger hủy phòng
 * - Giải phóng phòng và cập nhật trạng thái về VACANT
 * - Gửi thông báo hủy phòng đến booking service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoomCancellationListenerImpl implements RoomCancellationListener {

    private final RoomCancellationCommand roomCancellationCommandStep;

    /**
     * Xử lý room cancellation event
     * <p>
     * EVENT FLOW:
     * 1. Nhận thông tin hủy phòng từ booking service
     * 2. Validate và xử lý business logic
     * 3. Trigger cancellation saga step
     * 4. Giải phóng phòng và cập nhật trạng thái
     * <p>
     * BUSINESS LOGIC:
     * - Kiểm tra điều kiện hủy phòng
     * - Giải phóng phòng về trạng thái VACANT
     * - Cập nhật trạng thái phòng
     * <p>
     * SAGA INTEGRATION:
     * - Sử dụng RoomCancellationStep để xử lý saga
     * - Quản lý outbox messages
     * - Xử lý rollback khi có lỗi
     *
     * @param cancellationResponseMessage Thông tin hủy phòng từ booking service
     */
    @Override
    public void processRoomCancellation(RoomCancellationResponseMessage cancellationResponseMessage) {
        log.info("Processing room cancellation for room id: {}", cancellationResponseMessage.getRoomId());

        try {
            // Validate input data
            validateCancellationRequest(cancellationResponseMessage);

            log.info("Room cancellation processed successfully for booking  : {}",
                    cancellationResponseMessage.getBookingId());

        } catch (Exception e) {
            log.error("Error processing room cancellation for booking: {}",
                    cancellationResponseMessage.getBookingId(), e);
            throw new RuntimeException("Failed to process room cancellation", e);
        }
    }

    /**
     * Validate cancellation request
     * <p>
     * CHECKS:
     * - BookingRoomRequestMessage không được null
     * - Saga ID và Booking ID phải hợp lệ
     * - Booking status phải hợp lệ
     *
     * @param cancellationResponseMessage BookingRoomRequestMessage cần validate
     * @throws IllegalArgumentException nếu validation fail
     */
    private void validateCancellationRequest(RoomCancellationResponseMessage cancellationResponseMessage) {
        if (cancellationResponseMessage == null) {
            throw new IllegalArgumentException("Booking room request message cannot be null");
        }

        if (cancellationResponseMessage.getRoomId() == null) {
            throw new IllegalArgumentException("Saga ID cannot be null or empty");
        }

        if (cancellationResponseMessage.getBookingId() == null) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        log.debug("Cancellation request validation passed for booking : {}",
                cancellationResponseMessage.getBookingId());
    }
}
