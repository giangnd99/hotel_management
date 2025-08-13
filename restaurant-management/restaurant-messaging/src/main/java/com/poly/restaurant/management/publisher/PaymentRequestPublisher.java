package com.poly.restaurant.management.publisher;

import com.poly.restaurant.management.message.PaymentRequestMessage;

/**
 * Interface cho payment request publisher
 * 
 * NGHIỆP VỤ:
 * - Định nghĩa contract cho việc publish payment request messages
 * - Gửi yêu cầu thanh toán đến payment service
 * 
 * PATTERNS ÁP DỤNG:
 * - Publisher Pattern: Gửi messages
 * - Interface Pattern: Định nghĩa contract
 */
public interface PaymentRequestPublisher {

    /**
     * Publish payment request message
     * 
     * @param paymentRequestMessage Message chứa thông tin yêu cầu thanh toán
     */
    void publish(PaymentRequestMessage paymentRequestMessage);
}
