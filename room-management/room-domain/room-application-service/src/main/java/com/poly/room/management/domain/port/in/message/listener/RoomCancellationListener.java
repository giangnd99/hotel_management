package com.poly.room.management.domain.port.in.message.listener;

import com.poly.room.management.domain.message.BookingRoomRequestMessage;
import com.poly.room.management.domain.message.RoomCancellationRequestMessage;
import com.poly.room.management.domain.message.RoomCancellationResponseMessage;

/**
 * Room Cancellation Listener Interface
 * <p>
 * CHỨC NĂNG:
 * - Xử lý room cancellation events từ Kafka messages
 * - Quản lý quy trình hủy phòng trong hệ thống
 * - Tích hợp với Saga pattern để đảm bảo tính nhất quán dữ liệu
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông tin hủy phòng từ booking management service
 * - Thực hiện business logic hủy phòng
 * - Xử lý giải phóng phòng và cập nhật trạng thái
 * <p>
 * PATTERNS:
 * - Event-Driven Architecture: Xử lý messages từ Kafka
 * - Saga Pattern: Quản lý distributed transaction
 * - Outbox Pattern: Đảm bảo message delivery reliability
 */
public interface RoomCancellationListener {

    /**
     * Xử lý room cancellation event
     * <p>
     * BUSINESS LOGIC:
     * - Nhận thông tin hủy phòng từ booking service
     * - Validate điều kiện hủy phòng
     * - Thực hiện hủy phòng và cập nhật trạng thái
     * - Giải phóng phòng về trạng thái VACANT
     * <p>
     * SAGA INTEGRATION:
     * - Trigger cancellation saga step
     * - Quản lý outbox messages
     * - Xử lý rollback khi có lỗi
     *
     * @param bookingRoomRequestMessage Thông tin hủy phòng từ booking service
     */
    void processRoomCancellation(RoomCancellationResponseMessage bookingRoomRequestMessage);
}
