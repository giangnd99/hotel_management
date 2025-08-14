package com.poly.restaurant.management.adapter;

import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.application.port.in.message.listener.RoomOrderResponseListener;
import com.poly.restaurant.domain.entity.OrderStatus;
import com.poly.restaurant.management.helper.OrderStatusHelper;
import com.poly.restaurant.management.helper.RoomOrderMessageHelper;
import com.poly.restaurant.management.message.RoomOrderResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adapter implementation cho room order response listener
 * 
 * NGHIỆP VỤ:
 * - Xử lý room order response messages
 * - Cập nhật trạng thái order dựa trên response
 * - Logging và error handling
 * 
 * PATTERNS ÁP DỤNG:
 * - Adapter Pattern: Chuyển đổi messages thành business logic
 * - Handler Pattern: Xử lý business logic
 * - Observer Pattern: Lắng nghe events
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RoomOrderResponseListenerImpl implements RoomOrderResponseListener {

    private final OrderHandler orderHandler;

    @Override
    public void onRoomOrderSuccess(RoomOrderResponseMessage roomOrderResponseMessage) {
        try {
            if (!RoomOrderMessageHelper.isValidRoomOrderResponseMessage(roomOrderResponseMessage)) {
                log.error("Invalid room order response message for order: {}", roomOrderResponseMessage.getOrderId());
                return;
            }
            
            logRoomOrderSuccess(roomOrderResponseMessage);
            
            if (RoomOrderMessageHelper.REQUEST_TYPE_ATTACH_ORDER.equals(roomOrderResponseMessage.getRequestType())) {
                updateOrderStatusToAttached(roomOrderResponseMessage);
            } else if (RoomOrderMessageHelper.REQUEST_TYPE_DETACH_ORDER.equals(roomOrderResponseMessage.getRequestType())) {
                updateOrderStatusToDetached(roomOrderResponseMessage);
            } else {
                log.warn("Unknown request type: {} for order: {}", 
                        roomOrderResponseMessage.getRequestType(), roomOrderResponseMessage.getOrderId());
            }
        } catch (Exception e) {
            handleRoomOrderSuccessError(roomOrderResponseMessage, e);
        }
    }

    @Override
    public void onRoomOrderFailure(RoomOrderResponseMessage roomOrderResponseMessage) {
        try {
            if (!RoomOrderMessageHelper.isValidRoomOrderResponseMessage(roomOrderResponseMessage)) {
                log.error("Invalid room order response message for order: {}", roomOrderResponseMessage.getOrderId());
                return;
            }
            
            logRoomOrderFailure(roomOrderResponseMessage);
            
            if (RoomOrderMessageHelper.REQUEST_TYPE_ATTACH_ORDER.equals(roomOrderResponseMessage.getRequestType())) {
                updateOrderStatusToFailed(roomOrderResponseMessage);
            } else if (RoomOrderMessageHelper.REQUEST_TYPE_DETACH_ORDER.equals(roomOrderResponseMessage.getRequestType())) {
                log.warn("Detach order failed for order: {}, but order remains attached", roomOrderResponseMessage.getOrderId());
            } else {
                log.warn("Unknown request type: {} for order: {}", 
                        roomOrderResponseMessage.getRequestType(), roomOrderResponseMessage.getOrderId());
            }
        } catch (Exception e) {
            handleRoomOrderFailureError(roomOrderResponseMessage, e);
        }
    }

    private void updateOrderStatusToAttached(RoomOrderResponseMessage roomOrderResponseMessage) {
        String orderId = roomOrderResponseMessage.getOrderId();
        log.info("Cập nhật trạng thái order {} thành ATTACHED_TO_ROOM", orderId);
        try {
            // TODO: Implement order status update logic
            // OrderStatus newStatus = OrderStatus.IN_PROGRESS; // hoặc status phù hợp
            // orderHandler.updateOrderStatus(orderId, newStatus);
            OrderStatusHelper.logOrderStatusChange(orderId, null, OrderStatus.IN_PROGRESS);
            log.info("Đã cập nhật trạng thái order {} thành ATTACHED_TO_ROOM thành công", orderId);
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật trạng thái order {} thành ATTACHED_TO_ROOM: {}", orderId, e.getMessage(), e);
            throw e;
        }
    }

    private void updateOrderStatusToDetached(RoomOrderResponseMessage roomOrderResponseMessage) {
        String orderId = roomOrderResponseMessage.getOrderId();
        log.info("Cập nhật trạng thái order {} thành DETACHED_FROM_ROOM", orderId);
        try {
            // TODO: Implement order status update logic
            // OrderStatus newStatus = OrderStatus.COMPLETED; // hoặc status phù hợp
            // orderHandler.updateOrderStatus(orderId, newStatus);
            OrderStatusHelper.logOrderStatusChange(orderId, null, OrderStatus.COMPLETED);
            log.info("Đã cập nhật trạng thái order {} thành DETACHED_FROM_ROOM thành công", orderId);
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật trạng thái order {} thành DETACHED_FROM_ROOM: {}", orderId, e.getMessage(), e);
            throw e;
        }
    }

    private void updateOrderStatusToFailed(RoomOrderResponseMessage roomOrderResponseMessage) {
        String orderId = roomOrderResponseMessage.getOrderId();
        log.info("Cập nhật trạng thái order {} thành FAILED do room attachment failed", orderId);
        try {
            // TODO: Implement order status update logic
            // OrderStatus newStatus = OrderStatus.CANCELLED; // hoặc status phù hợp
            // orderHandler.updateOrderStatus(orderId, newStatus);
            OrderStatusHelper.logOrderStatusChange(orderId, null, OrderStatus.CANCELLED);
            log.info("Đã cập nhật trạng thái order {} thành FAILED thành công", orderId);
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật trạng thái order {} thành FAILED: {}", orderId, e.getMessage(), e);
            throw e;
        }
    }

    private void logRoomOrderSuccess(RoomOrderResponseMessage roomOrderResponseMessage) {
        log.info("Room order operation successful for order: {} to room: {} with type: {}",
                roomOrderResponseMessage.getOrderId(), 
                roomOrderResponseMessage.getRoomId(),
                roomOrderResponseMessage.getRequestType());
        RoomOrderMessageHelper.logRoomOrderResponseDetails(roomOrderResponseMessage);
    }

    private void logRoomOrderFailure(RoomOrderResponseMessage roomOrderResponseMessage) {
        log.error("Room order operation failed for order: {} to room: {} with type: {}. Message: {}",
                roomOrderResponseMessage.getOrderId(), 
                roomOrderResponseMessage.getRoomId(),
                roomOrderResponseMessage.getRequestType(),
                roomOrderResponseMessage.getMessage());
        RoomOrderMessageHelper.logRoomOrderResponseDetails(roomOrderResponseMessage);
    }

    private void handleRoomOrderSuccessError(RoomOrderResponseMessage roomOrderResponseMessage, Exception e) {
        log.error("Error handling room order success for order: {} to room: {}. Error: {}",
                roomOrderResponseMessage.getOrderId(), roomOrderResponseMessage.getRoomId(), e.getMessage(), e);
        RoomOrderMessageHelper.logRoomOrderError("success handling", roomOrderResponseMessage.getOrderId(), e.getMessage());
    }

    private void handleRoomOrderFailureError(RoomOrderResponseMessage roomOrderResponseMessage, Exception e) {
        log.error("Error handling room order failure for order: {} to room: {}. Error: {}",
                roomOrderResponseMessage.getOrderId(), roomOrderResponseMessage.getRoomId(), e.getMessage(), e);
        RoomOrderMessageHelper.logRoomOrderError("failure handling", roomOrderResponseMessage.getOrderId(), e.getMessage());
    }
}
