package com.poly.booking.management.domain.port.in.message.listener;

import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;

/**
 * Booking Cancellation Listener Interface
 * <p>
 * CHỨC NĂNG:
 * - Xử lý booking cancellation events từ Kafka messages
 * - Quản lý quy trình hủy booking trong hệ thống
 * - Tích hợp với Saga pattern để đảm bảo tính nhất quán dữ liệu
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông tin hủy booking từ room management service
 * - Thực hiện business logic hủy booking
 * - Xử lý hoàn tiền theo chính sách của khách sạn
 * <p>
 * PATTERNS:
 * - Event-Driven Architecture: Xử lý messages từ Kafka
 * - Saga Pattern: Quản lý distributed transaction
 * - Outbox Pattern: Đảm bảo message delivery reliability
 */
public interface BookingCancellationListener {

    /**
     * Xử lý booking cancellation event
     * <p>
     * BUSINESS LOGIC:
     * - Nhận thông tin hủy booking từ room service
     * - Validate điều kiện hủy booking
     * - Thực hiện hủy booking và cập nhật trạng thái
     * - Xử lý hoàn tiền nếu đủ điều kiện
     * <p>
     * SAGA INTEGRATION:
     * - Trigger cancellation saga step
     * - Quản lý outbox messages
     * - Xử lý rollback khi có lỗi
     *
     * @param roomMessageResponse Thông tin hủy booking từ room service
     */
    void processBookingCancellation(RoomMessageResponse roomMessageResponse);

    /**
     * Xử lý booking cancellation rollback
     * <p>
     * ROLLBACK LOGIC:
     * - Thực hiện rollback khi cancellation gặp lỗi
     * - Revert booking status về trạng thái trước đó
     * - Cập nhật outbox messages
     * <p>
     * COMPENSATION:
     * - Đảm bảo tính nhất quán dữ liệu
     * - Xử lý các side effects của cancellation
     *
     * @param roomMessageResponse Thông tin rollback từ room service
     */
    void processBookingCancellationRollback(RoomMessageResponse roomMessageResponse);
}
