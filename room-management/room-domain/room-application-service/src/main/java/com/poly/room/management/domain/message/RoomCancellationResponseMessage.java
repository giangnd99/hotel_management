package com.poly.room.management.domain.message;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoomCancellationResponseMessage {
    private UUID bookingId;

    /**
     * ID của phòng đã được hủy
     */
    private UUID roomId;

    /**
     * Trạng thái hủy phòng (ví dụ: SUCCESS, FAILED)
     */
    private String cancellationStatus;

    /**
     * Thông báo chi tiết về kết quả hủy
     */
    private String message;
}
