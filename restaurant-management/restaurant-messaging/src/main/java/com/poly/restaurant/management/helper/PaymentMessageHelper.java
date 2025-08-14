package com.poly.restaurant.management.helper;

import com.poly.restaurant.management.message.PaymentRequestMessage;
import com.poly.restaurant.management.message.PaymentResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Helper class chứa các utility methods cho payment message processing
 * 
 * NGHIỆP VỤ:
 * - Validate payment messages
 * - Tạo payment request messages
 * - Xử lý payment status validation
 * - Cung cấp các utility methods cho payment operations
 * 
 * PATTERNS ÁP DỤNG:
 * - Utility Pattern: Tập trung các helper methods
 * - Validation Pattern: Centralized validation logic
 * - Factory Pattern: Tạo payment messages
 */
@Slf4j
public class PaymentMessageHelper {

    /**
     * Validate payment request message
     */
    public static boolean isValidPaymentRequestMessage(PaymentRequestMessage message) {
        if (message == null) {
            log.warn("PaymentRequestMessage is null");
            return false;
        }

        if (!StringUtils.hasText(message.getId())) {
            log.warn("PaymentRequestMessage id is empty");
            return false;
        }

        if (!StringUtils.hasText(message.getOrderId())) {
            log.warn("PaymentRequestMessage orderId is empty");
            return false;
        }

        if (!StringUtils.hasText(message.getAmount())) {
            log.warn("PaymentRequestMessage amount is empty");
            return false;
        }

        if (!isValidAmount(message.getAmount())) {
            log.warn("PaymentRequestMessage amount is invalid: {}", message.getAmount());
            return false;
        }

        if (!StringUtils.hasText(message.getOrderPaymentStatus())) {
            log.warn("PaymentRequestMessage orderPaymentStatus is empty");
            return false;
        }

        if (!StringUtils.hasText(message.getPaymentMethod())) {
            log.warn("PaymentRequestMessage paymentMethod is empty");
            return false;
        }

        return true;
    }

    /**
     * Validate payment response message
     */
    public static boolean isValidPaymentResponseMessage(PaymentResponseMessage message) {
        if (message == null) {
            log.warn("PaymentResponseMessage is null");
            return false;
        }

        if (!StringUtils.hasText(message.getId())) {
            log.warn("PaymentResponseMessage id is empty");
            return false;
        }

        if (!StringUtils.hasText(message.getOrderId())) {
            log.warn("PaymentResponseMessage orderId is empty");
            return false;
        }

        if (!StringUtils.hasText(message.getOrderPaymentStatus())) {
            log.warn("PaymentResponseMessage orderPaymentStatus is empty");
            return false;
        }

        if (!StringUtils.hasText(message.getPaymentMethod())) {
            log.warn("PaymentResponseMessage paymentMethod is empty");
            return false;
        }

        return true;
    }

    /**
     * Validate amount string
     */
    public static boolean isValidAmount(String amount) {
        if (!StringUtils.hasText(amount)) {
            return false;
        }

        try {
            BigDecimal amountValue = new BigDecimal(amount);
            return amountValue.compareTo(BigDecimal.ZERO) > 0;
        } catch (NumberFormatException e) {
            log.warn("Invalid amount format: {}", amount);
            return false;
        }
    }

    /**
     * Tạo payment request message
     */
    public static PaymentRequestMessage createPaymentRequestMessage(String orderId, 
                                                                   String amount, 
                                                                   String paymentMethod) {
        return PaymentRequestMessage.builder()
                .id(UUID.randomUUID().toString())
                .orderId(orderId)
                .amount(amount)
                .orderPaymentStatus("PENDING")
                .paymentMethod(paymentMethod)
                .build();
    }

    /**
     * Kiểm tra payment status có hợp lệ không
     */
    public static boolean isValidPaymentStatus(String status) {
        if (!StringUtils.hasText(status)) {
            return false;
        }

        return "COMPLETED".equals(status) || 
               "FAILED".equals(status) || 
               "CANCELLED".equals(status) ||
               "PENDING".equals(status);
    }

    /**
     * Kiểm tra payment có thành công không
     */
    public static boolean isPaymentSuccessful(String status) {
        return "COMPLETED".equals(status);
    }

    /**
     * Kiểm tra payment có thất bại không
     */
    public static boolean isPaymentFailed(String status) {
        return "FAILED".equals(status) || "CANCELLED".equals(status);
    }

    /**
     * Log payment message details
     */
    public static void logPaymentMessageDetails(String messageType, String orderId, String status) {
        log.info("{} - Order ID: {}, Status: {}", messageType, orderId, status);
    }

    /**
     * Log payment error
     */
    public static void logPaymentError(String operation, String orderId, String error) {
        log.error("Payment {} failed for order {}: {}", operation, orderId, error);
    }
}
