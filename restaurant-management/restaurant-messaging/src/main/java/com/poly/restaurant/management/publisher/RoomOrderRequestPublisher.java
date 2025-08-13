package com.poly.restaurant.management.publisher;

import com.poly.restaurant.management.message.RoomOrderRequestMessage;

/**
 * Interface cho room order request publisher
 * 
 * NGHIỆP VỤ:
 * - Định nghĩa contract cho việc publish room order request messages
 * - Gửi yêu cầu đính kèm/gỡ order khỏi room
 * 
 * PATTERNS ÁP DỤNG:
 * - Publisher Pattern: Gửi messages
 * - Interface Pattern: Định nghĩa contract
 */
public interface RoomOrderRequestPublisher {

    /**
     * Publish room order request message
     * 
     * @param roomOrderRequestMessage Message chứa thông tin yêu cầu
     */
    void publish(RoomOrderRequestMessage roomOrderRequestMessage);
}
