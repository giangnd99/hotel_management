package com.poly.restaurant.application.port.in.message.listener;

import com.poly.restaurant.management.message.RoomOrderResponseMessage;

/**
 * Interface cho room order response listener
 * 
 * NGHIỆP VỤ:
 * - Định nghĩa contract cho việc xử lý room order response messages
 * - Nhận kết quả từ room service về việc đính kèm/gỡ order
 * 
 * PATTERNS ÁP DỤNG:
 * - Listener Pattern: Nhận và xử lý messages
 * - Interface Pattern: Định nghĩa contract
 */
public interface RoomOrderResponseListener {

    /**
     * Xử lý room order response thành công
     * 
     * @param roomOrderResponseMessage Message chứa kết quả thành công
     */
    void onRoomOrderSuccess(RoomOrderResponseMessage roomOrderResponseMessage);

    /**
     * Xử lý room order response thất bại
     * 
     * @param roomOrderResponseMessage Message chứa kết quả thất bại
     */
    void onRoomOrderFailure(RoomOrderResponseMessage roomOrderResponseMessage);
}
