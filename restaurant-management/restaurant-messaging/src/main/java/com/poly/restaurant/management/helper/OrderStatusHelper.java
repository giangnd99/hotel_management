package com.poly.restaurant.management.helper;

import com.poly.restaurant.domain.entity.OrderStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * Helper class để xử lý các trạng thái order và chuyển đổi giữa payment status và order status
 * 
 * NGHIỆP VỤ:
 * - Chuyển đổi payment status thành order status
 * - Validate order status transitions
 * - Cung cấp các utility methods cho order status management
 * 
 * PATTERNS ÁP DỤNG:
 * - Utility Pattern: Tập trung các helper methods
 * - State Pattern: Quản lý trạng thái order
 * - Mapping Pattern: Chuyển đổi giữa các loại status
 */
@Slf4j
public class OrderStatusHelper {

    /**
     * Chuyển đổi payment status thành order status
     */
    public static OrderStatus mapPaymentStatusToOrderStatus(String paymentStatus) {
        if (paymentStatus == null) {
            log.warn("Payment status is null, returning CANCELLED");
            return OrderStatus.CANCELLED;
        }

        switch (paymentStatus.toUpperCase()) {
            case "COMPLETED":
                return OrderStatus.COMPLETED;
            case "FAILED":
            case "CANCELLED":
                return OrderStatus.CANCELLED;
            case "PENDING":
                return OrderStatus.IN_PROGRESS;
            default:
                log.warn("Unknown payment status: {}, returning CANCELLED", paymentStatus);
                return OrderStatus.CANCELLED;
        }
    }

    /**
     * Kiểm tra xem order status transition có hợp lệ không
     */
    public static boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (currentStatus == null || newStatus == null) {
            return false;
        }

        switch (currentStatus) {
            case NEW:
                return newStatus == OrderStatus.IN_PROGRESS || 
                       newStatus == OrderStatus.CANCELLED ||
                       newStatus == OrderStatus.COMPLETED;
            case IN_PROGRESS:
                return newStatus == OrderStatus.COMPLETED || 
                       newStatus == OrderStatus.CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false; // Terminal states
            default:
                return false;
        }
    }

    /**
     * Kiểm tra xem order có thể được cập nhật không
     */
    public static boolean canUpdateOrder(OrderStatus currentStatus) {
        return currentStatus != OrderStatus.COMPLETED && 
               currentStatus != OrderStatus.CANCELLED;
    }

    /**
     * Kiểm tra xem order có thể được hủy không
     */
    public static boolean canCancelOrder(OrderStatus currentStatus) {
        return currentStatus == OrderStatus.NEW || 
               currentStatus == OrderStatus.IN_PROGRESS;
    }

    /**
     * Kiểm tra xem order có thể được hoàn thành không
     */
    public static boolean canCompleteOrder(OrderStatus currentStatus) {
        return currentStatus == OrderStatus.IN_PROGRESS;
    }

    /**
     * Lấy description cho order status
     */
    public static String getOrderStatusDescription(OrderStatus status) {
        if (status == null) {
            return "UNKNOWN";
        }

        switch (status) {
            case NEW:
                return "Đơn hàng mới";
            case IN_PROGRESS:
                return "Đang xử lý";
            case COMPLETED:
                return "Đã hoàn thành";
            case CANCELLED:
                return "Đã hủy";
            default:
                return "Không xác định";
        }
    }

    /**
     * Log order status change
     */
    public static void logOrderStatusChange(String orderId, OrderStatus oldStatus, OrderStatus newStatus) {
        log.info("Order {} status changed from {} to {}", 
                orderId, 
                getOrderStatusDescription(oldStatus), 
                getOrderStatusDescription(newStatus));
    }

    /**
     * Log invalid status transition attempt
     */
    public static void logInvalidStatusTransition(String orderId, OrderStatus currentStatus, OrderStatus attemptedStatus) {
        log.warn("Invalid status transition attempted for order {}: {} -> {}", 
                orderId, 
                getOrderStatusDescription(currentStatus), 
                getOrderStatusDescription(attemptedStatus));
    }

    /**
     * Kiểm tra xem order status có phải là terminal state không
     */
    public static boolean isTerminalState(OrderStatus status) {
        return status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED;
    }

    /**
     * Kiểm tra xem order có thể được thanh toán không
     */
    public static boolean canProcessPayment(OrderStatus status) {
        return status == OrderStatus.NEW || status == OrderStatus.IN_PROGRESS;
    }
}
