package com.poly.room.management.domain.port.out.publisher.request;

import com.poly.room.management.domain.message.BookingRoomRequestMessage;

/**
 * Room Check Out Request Publisher Interface
 * <p>
 * CHỨC NĂNG:
 * - Định nghĩa contract cho việc gửi room check out request messages
 * - Gửi thông tin checkout phòng đến Kafka topic
 * - Đảm bảo tính nhất quán dữ liệu thông qua messaging pattern
 * <p>
 * MỤC ĐÍCH:
 * - Tách biệt business logic khỏi messaging infrastructure
 * - Cung cấp abstraction layer cho việc gửi messages
 * - Hỗ trợ testing và mocking
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Port-Adapter Pattern: Định nghĩa contract cho messaging
 * - Dependency Inversion: Business logic không phụ thuộc vào messaging implementation
 */
public interface RoomCheckOutRequestPublisher {

    /**
     * Gửi room check out request đến Kafka topic
     * <p>
     * NGHIỆP VỤ:
     * - Gửi thông tin checkout phòng đến các service khác
     * - Đảm bảo tính nhất quán dữ liệu thông qua messaging pattern
     * <p>
     * FLOW XỬ LÝ:
     * 1. Nhận room check out request message
     * 2. Gửi message đến Kafka topic
     * 3. Xử lý lỗi nếu có vấn đề xảy ra
     *
     * @param bookingRoomRequestMessage Message chứa thông tin checkout phòng
     */
    void publish(BookingRoomRequestMessage bookingRoomRequestMessage);
}
