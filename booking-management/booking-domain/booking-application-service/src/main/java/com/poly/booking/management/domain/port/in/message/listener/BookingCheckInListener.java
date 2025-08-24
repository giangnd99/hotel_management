package com.poly.booking.management.domain.port.in.message.listener;

import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;

/**
 * Booking Check-In Listener Interface
 * <p>
 * CHỨC NĂNG:
 * - Xử lý các sự kiện check-in từ notification service
 * - Cập nhật trạng thái booking khi khách hàng check-in
 * - Trigger các business logic liên quan đến check-in
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông báo check-in từ room management service
 * - Thực hiện business logic check-in
 * - Cập nhật trạng thái booking và gửi thông báo xác nhận
 */
public interface BookingCheckInListener {

    /**
     * Xử lý check-in thành công
     * <p>
     * BUSINESS LOGIC:
     * - Cập nhật trạng thái booking thành CHECKED_IN
     * - Ghi log check-in thành công
     * - Gửi thông báo xác nhận check-in
     * - Cập nhật thời gian check-in thực tế
     *
     * @param notificationMessageResponse Thông tin check-in từ notification service
     */
    void processBookingCheckIn(NotificationMessageResponse notificationMessageResponse);

    /**
     * Xử lý check-in thất bại
     * <p>
     * BUSINESS LOGIC:
     * - Ghi log check-in thất bại
     * - Gửi thông báo lỗi check-in
     * - Cập nhật trạng thái booking nếu cần
     *
     * @param notificationMessageResponse Thông tin check-in thất bại
     */
    void processBookingCheckInFailed(NotificationMessageResponse notificationMessageResponse);

    /**
     * Xử lý check-in đang chờ
     * <p>
     * BUSINESS LOGIC:
     * - Ghi log check-in đang chờ
     * - Gửi thông báo chờ xử lý
     * - Cập nhật trạng thái booking thành PENDING_CHECKIN
     *
     * @param notificationMessageResponse Thông tin check-in đang chờ
     */
    void processBookingCheckInPending(NotificationMessageResponse notificationMessageResponse);
}
