package com.poly.restaurant.management.message;

import lombok.*;

/**
 * Message để gửi yêu cầu đính kèm order vào room
 * 
 * NGHIỆP VỤ:
 * - Gửi thông tin order cần đính kèm vào room
 * - Chứa thông tin room và order để room service xử lý
 * 
 * PATTERNS ÁP DỤNG:
 * - Message Pattern: Truyền thông tin giữa các service
 * - DTO Pattern: Data transfer object
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomOrderRequestMessage {

    private String id;
    private String orderId;
    private String roomId;
    private String customerId;
    private String amount;
    private String orderStatus;
    private String requestType; // "ATTACH_ORDER" hoặc "DETACH_ORDER"
}
