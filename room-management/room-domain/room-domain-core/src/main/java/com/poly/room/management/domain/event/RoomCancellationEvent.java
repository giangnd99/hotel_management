package com.poly.room.management.domain.event;

import com.poly.room.management.domain.entity.Room;
import com.poly.domain.event.DomainEvent;

import java.time.ZonedDateTime;

/**
 * Room Cancellation Event - Sự kiện hủy phòng
 * <p>
 * CHỨC NĂNG:
 * - Đánh dấu phòng đã bị hủy khỏi booking
 * - Chứa thông tin về lý do hủy và thời gian hủy
 * - Trigger các business logic liên quan đến hủy phòng
 * <p>
 * MỤC ĐÍCH:
 * - Thông báo cho các service khác về việc hủy phòng
 * - Xử lý compensation logic (giải phóng phòng, cập nhật trạng thái)
 * - Cập nhật trạng thái hệ thống
 */
public class RoomCancellationEvent implements DomainEvent<Room> {

    private final Room room;
    private final String bookingId;
    private final String cancellationReason;
    private final ZonedDateTime cancelledAt;

    public RoomCancellationEvent(Room room, String bookingId, String cancellationReason) {
        this.room = room;
        this.bookingId = bookingId;
        this.cancellationReason = cancellationReason;
        this.cancelledAt = ZonedDateTime.now();
    }

    public Room getRoom() {
        return room;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public ZonedDateTime getCancelledAt() {
        return cancelledAt;
    }
}
