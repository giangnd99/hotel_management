package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.domain.valueobject.DateCustom;

import java.time.ZonedDateTime;

/**
 * Booking Cancelled Event - Sự kiện hủy booking
 * <p>
 * CHỨC NĂNG:
 * - Đánh dấu booking đã bị hủy
 * - Chứa thông tin về lý do hủy và thời gian hủy
 * - Trigger các business logic liên quan đến hủy booking
 * <p>
 * MỤC ĐÍCH:
 * - Thông báo cho các service khác về việc hủy booking
 * - Xử lý compensation logic (hoàn tiền, giải phóng phòng)
 * - Cập nhật trạng thái hệ thống
 */
public class BookingCancelledEvent extends BookingEvent {

    private final String cancellationReason;
    private final boolean isRefundable;
    private final ZonedDateTime cancelledAt;

    public BookingCancelledEvent(Booking booking, String cancellationReason, boolean isRefundable) {
        super(booking, DateCustom.now());
        this.cancellationReason = cancellationReason;
        this.isRefundable = isRefundable;
        this.cancelledAt = ZonedDateTime.now();
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public ZonedDateTime getCancelledAt() {
        return cancelledAt;
    }


}
