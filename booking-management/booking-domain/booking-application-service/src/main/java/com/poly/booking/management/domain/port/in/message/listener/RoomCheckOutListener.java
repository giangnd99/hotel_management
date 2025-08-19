package com.poly.booking.management.domain.port.in.message.listener;

import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;

/**
 * Room Check Out Listener Interface
 * <p>
 * CHỨC NĂNG:
 * - Xử lý room check out events từ Kafka messages
 * - Quản lý quy trình checkout phòng trong hệ thống booking
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông tin checkout phòng từ room management service
 * - Thực hiện business logic checkout cho booking
 * - Cập nhật trạng thái booking sang CHECKED_OUT
 */
public interface RoomCheckOutListener {

    /**
     * Xử lý room check out event
     * <p>
     * Nhận thông tin checkout phòng và thực hiện checkout cho booking
     *
     * @param roomCheckOutResponseMessage Thông tin checkout phòng từ room service
     */
    void roomCheckOutCompleted(RoomMessageResponse roomCheckOutResponseMessage);

    /**
     * Xử lý room check out cancelled event
     * <p>
     * Xử lý khi việc checkout phòng bị hủy bỏ
     *
     * @param roomCheckOutResponseMessage Thông tin checkout phòng bị hủy
     */
    void roomCheckOutCancelled(RoomMessageResponse roomCheckOutResponseMessage);
}
