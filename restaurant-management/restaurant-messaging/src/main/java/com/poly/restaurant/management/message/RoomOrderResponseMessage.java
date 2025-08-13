package com.poly.restaurant.management.message;

import lombok.*;

/**
 * Message để nhận response từ room service về việc đính kèm order
 * 
 * NGHIỆP VỤ:
 * - Nhận kết quả xử lý từ room service
 * - Chứa thông tin trạng thái và thông báo lỗi nếu có
 * 
 * PATTERNS ÁP DỤNG:
 * - Message Pattern: Truyền thông tin giữa các service
 * - DTO Pattern: Data transfer object
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomOrderResponseMessage {

    private String id;
    private String orderId;
    private String roomId;
    private String customerId;
    private String responseStatus; // "SUCCESS", "FAILED", "CANCELLED"
    private String message;
    private String requestType;
}
