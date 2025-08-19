package com.poly.booking.management.messaging.adapter.listener;

import com.poly.booking.management.domain.port.in.message.listener.RoomCheckOutListener;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.saga.room.RoomCheckOutStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Room Check Out Listener Implementation
 * <p>
 * CHỨC NĂNG:
 * - Xử lý room check out events từ Kafka messages
 * - Quản lý quy trình checkout phòng trong hệ thống booking
 * - Tích hợp với Saga pattern để đảm bảo tính nhất quán dữ liệu
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông tin checkout phòng từ room management service
 * - Thực hiện business logic checkout cho booking
 * - Cập nhật trạng thái booking sang CHECKED_OUT
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Saga Pattern: Quản lý distributed transaction
 * - Event Driven Architecture: Xử lý bất đồng bộ
 * - Clean Architecture: Tách biệt business logic
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoomCheckOutListenerImpl implements RoomCheckOutListener {

    private final RoomCheckOutStep roomCheckOutStep;

    /**
     * Xử lý room check out completed event
     * <p>
     * Nhận thông tin checkout phòng thành công và thực hiện checkout cho booking
     *
     * @param roomCheckOutResponseMessage Thông tin checkout phòng từ room service
     */
    @Override
    public void roomCheckOutCompleted(RoomMessageResponse roomCheckOutResponseMessage) {
        log.info("Processing room check out completed event for booking: {}", 
                roomCheckOutResponseMessage.getBookingId());
        
        try {
            // Thực hiện saga step để xử lý checkout
            roomCheckOutStep.process(roomCheckOutResponseMessage);
            
            log.info("Room check out completed successfully for booking: {}", 
                    roomCheckOutResponseMessage.getBookingId());
                    
        } catch (Exception e) {
            log.error("Error processing room check out completed event for booking: {}", 
                    roomCheckOutResponseMessage.getBookingId(), e);
            throw new RuntimeException("Failed to process room check out completed event", e);
        }
    }

    /**
     * Xử lý room check out cancelled event
     * <p>
     * Xử lý khi việc checkout phòng bị hủy bỏ
     *
     * @param roomCheckOutResponseMessage Thông tin checkout phòng bị hủy
     */
    @Override
    public void roomCheckOutCancelled(RoomMessageResponse roomCheckOutResponseMessage) {
        log.info("Processing room check out cancelled event for booking: {}", 
                roomCheckOutResponseMessage.getBookingId());
        
        try {
            // Thực hiện rollback saga step
            roomCheckOutStep.rollback(roomCheckOutResponseMessage);
            
            log.info("Room check out cancelled successfully for booking: {}", 
                    roomCheckOutResponseMessage.getBookingId());
                    
        } catch (Exception e) {
            log.error("Error processing room check out cancelled event for booking: {}", 
                    roomCheckOutResponseMessage.getBookingId(), e);
            throw new RuntimeException("Failed to process room check out cancelled event", e);
        }
    }
}
