package com.poly.booking.management.domain.port.out.message.publisher;

import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.outbox.OutboxStatus;

import java.util.function.BiConsumer;

/**
 * Room Check Out Message Publisher Interface
 * <p>
 * CHỨC NĂNG:
 * - Định nghĩa contract cho việc gửi room check out messages
 * - Gửi thông tin checkout phòng đến Kafka topic
 * - Đảm bảo tính nhất quán dữ liệu thông qua Outbox Pattern
 * <p>
 * MỤC ĐÍCH:
 * - Tách biệt business logic khỏi messaging infrastructure
 * - Cung cấp abstraction layer cho việc gửi messages
 * - Hỗ trợ testing và mocking
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Port-Adapter Pattern: Định nghĩa contract cho messaging
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Dependency Inversion: Business logic không phụ thuộc vào messaging implementation
 */
public interface RoomCheckOutMessagePublisher {

    /**
     * Gửi room check out request đến Kafka topic
     * <p>
     * NGHIỆP VỤ:
     * - Gửi thông tin checkout phòng đến room management service
     * - Đảm bảo tính nhất quán dữ liệu thông qua outbox pattern
     * - Xử lý callback để cập nhật trạng thái outbox message
     * <p>
     * FLOW XỬ LÝ:
     * 1. Nhận room outbox message từ outbox service
     * 2. Gửi message đến Kafka topic "room-check-out-topic"
     * 3. Cập nhật trạng thái outbox message thông qua callback
     * 4. Xử lý lỗi nếu có vấn đề xảy ra
     *
     * @param roomOutboxMessage Message chứa thông tin checkout phòng từ outbox
     * @param outboxCallback    Callback function để cập nhật trạng thái outbox
     */
    void sendRoomCheckInRequest(RoomOutboxMessage roomOutboxMessage,
                                BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback);
}
