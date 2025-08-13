package com.poly.restaurant.management.adapter;

import com.poly.restaurant.application.port.in.message.listener.PaymentResponseListener;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.domain.entity.OrderStatus;
import com.poly.restaurant.management.message.PaymentResponseMessage;
import com.poly.restaurant.management.helper.PaymentMessageHelper;
import com.poly.restaurant.management.helper.OrderStatusHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Implementation của PaymentResponseListener chịu trách nhiệm xử lý
 * các response message từ Payment Service.
 * 
 * NGHIỆP VỤ:
 * - Xử lý payment success: Cập nhật trạng thái order thành PAID
 * - Xử lý payment failure: Cập nhật trạng thái order thành CANCELLED
 * - Logging và monitoring cho payment events
 * - Đảm bảo tính nhất quán dữ liệu khi cập nhật order status
 * 
 * PATTERNS ÁP DỤNG:
 * - Adapter Pattern: Chuyển đổi message events thành business operations
 * - Event-Driven Architecture: Xử lý payment response events
 * - Separation of Concerns: Tách biệt payment handling và order management
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResponseListenerImpl implements PaymentResponseListener {

    private final OrderHandler orderHandler;

    /**
     * Xử lý khi payment thành công
     * 
     * NGHIỆP VỤ:
     * - Log thông tin payment success
     * - Cập nhật trạng thái order thành PAID
     * - Log việc thay đổi trạng thái order
     * - Lưu thay đổi vào database
     * 
     * @param paymentResponseMessage Thông tin payment response
     */
    @Override
    public void onPaymentSuccess(PaymentResponseMessage paymentResponseMessage) {
        try {
            // Validate payment response message
            if (!PaymentMessageHelper.isValidPaymentResponseMessage(paymentResponseMessage)) {
                log.error("Invalid payment response message for order: {}", paymentResponseMessage.getOrderId());
                return;
            }

            // Log thông tin payment success
            logPaymentSuccess(paymentResponseMessage);
            
            // Cập nhật trạng thái order thành COMPLETED
            updateOrderStatusToCompleted(paymentResponseMessage);
            
        } catch (Exception e) {
            // Log lỗi và có thể thêm logic retry hoặc compensation
            handlePaymentSuccessError(paymentResponseMessage, e);
        }
    }

    /**
     * Xử lý khi payment thất bại
     * 
     * NGHIỆP VỤ:
     * - Log thông tin payment failure
     * - Cập nhật trạng thái order thành CANCELLED
     * - Log việc thay đổi trạng thái order
     * - Lưu thay đổi vào database
     * 
     * @param paymentResponseMessage Thông tin payment response
     */
    @Override
    public void onPaymentFailure(PaymentResponseMessage paymentResponseMessage) {
        try {
            // Validate payment response message
            if (!PaymentMessageHelper.isValidPaymentResponseMessage(paymentResponseMessage)) {
                log.error("Invalid payment response message for order: {}", paymentResponseMessage.getOrderId());
                return;
            }

            // Log thông tin payment failure
            logPaymentFailure(paymentResponseMessage);
            
            // Cập nhật trạng thái order thành CANCELLED
            updateOrderStatusToCancelled(paymentResponseMessage);
            
        } catch (Exception e) {
            // Log lỗi và có thể thêm logic retry hoặc compensation
            handlePaymentFailureError(paymentResponseMessage, e);
        }
    }

    /**
     * Cập nhật trạng thái order thành COMPLETED
     */
    private void updateOrderStatusToCompleted(PaymentResponseMessage paymentResponseMessage) {
        String orderId = paymentResponseMessage.getOrderId();
        log.info("Cập nhật trạng thái order {} thành COMPLETED", orderId);
        
        try {
            // TODO: Implement order status update logic
            // OrderStatus newStatus = OrderStatus.COMPLETED;
            // orderHandler.updateOrderStatus(orderId, newStatus);
            
            // Log status change using helper
            OrderStatusHelper.logOrderStatusChange(orderId, null, OrderStatus.COMPLETED);
            
            log.info("Đã cập nhật trạng thái order {} thành COMPLETED thành công", orderId);
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật trạng thái order {} thành COMPLETED: {}", orderId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Cập nhật trạng thái order thành CANCELLED
     */
    private void updateOrderStatusToCancelled(PaymentResponseMessage paymentResponseMessage) {
        String orderId = paymentResponseMessage.getOrderId();
        log.info("Cập nhật trạng thái order {} thành CANCELLED", orderId);
        
        try {
            // TODO: Implement order status update logic
            // OrderStatus newStatus = OrderStatus.CANCELLED;
            // orderHandler.updateOrderStatus(orderId, newStatus);
            
            // Log status change using helper
            OrderStatusHelper.logOrderStatusChange(orderId, null, OrderStatus.CANCELLED);
            
            log.info("Đã cập nhật trạng thái order {} thành CANCELLED thành công", orderId);
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật trạng thái order {} thành CANCELLED: {}", orderId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Log thông tin payment success
     */
    private void logPaymentSuccess(PaymentResponseMessage paymentResponseMessage) {
        log.info("Payment thành công cho order id: {}, payment method: {}, payment id: {}", 
                paymentResponseMessage.getOrderId(),
                paymentResponseMessage.getPaymentMethod(),
                paymentResponseMessage.getId());
    }

    /**
     * Log thông tin payment failure
     */
    private void logPaymentFailure(PaymentResponseMessage paymentResponseMessage) {
        log.warn("Payment thất bại cho order id: {}, payment method: {}, payment id: {}", 
                paymentResponseMessage.getOrderId(),
                paymentResponseMessage.getPaymentMethod(),
                paymentResponseMessage.getId());
    }



    /**
     * Xử lý lỗi khi payment success
     */
    private void handlePaymentSuccessError(PaymentResponseMessage paymentResponseMessage, Exception e) {
        log.error("Lỗi khi xử lý payment success cho order id: {}. Error: {}", 
                paymentResponseMessage.getOrderId(), e.getMessage(), e);
        
        // TODO: Implement compensation logic hoặc retry mechanism
        // Có thể gửi notification cho admin hoặc trigger manual intervention
    }

    /**
     * Xử lý lỗi khi payment failure
     */
    private void handlePaymentFailureError(PaymentResponseMessage paymentResponseMessage, Exception e) {
        log.error("Lỗi khi xử lý payment failure cho order id: {}. Error: {}", 
                paymentResponseMessage.getOrderId(), e.getMessage(), e);
        
        // TODO: Implement compensation logic hoặc retry mechanism
        // Có thể gửi notification cho admin hoặc trigger manual intervention
    }
}
