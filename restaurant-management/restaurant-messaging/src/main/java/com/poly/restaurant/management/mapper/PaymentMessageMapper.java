package com.poly.restaurant.management.mapper;

import com.poly.restaurant.management.message.PaymentRequestMessage;
import com.poly.restaurant.management.message.PaymentResponseMessage;
import com.poly.restaurant.management.message.PaymentRequestMessageAvro;
import com.poly.restaurant.management.message.PaymentResponseMessageAvro;
import org.springframework.stereotype.Component;

/**
 * Mapper chịu trách nhiệm chuyển đổi giữa domain message và Avro message
 * cho payment operations trong restaurant module.
 * 
 * NGHIỆP VỤ:
 * - Chuyển đổi PaymentRequestMessage <-> PaymentRequestMessageAvro
 * - Chuyển đổi PaymentResponseMessage <-> PaymentResponseMessageAvro
 * - Đảm bảo tính nhất quán dữ liệu khi serialize/deserialize
 */
@Component
public class PaymentMessageMapper {

    /**
     * Chuyển đổi PaymentRequestMessage thành PaymentRequestMessageAvro
     */
    public PaymentRequestMessageAvro paymentRequestMessageToPaymentRequestMessageAvro(
            PaymentRequestMessage paymentRequestMessage) {
        
        if (paymentRequestMessage == null) {
            return null;
        }

        return PaymentRequestMessageAvro.newBuilder()
                .setId(paymentRequestMessage.getId())
                .setOrderId(paymentRequestMessage.getOrderId())
                .setAmount(paymentRequestMessage.getAmount())
                .setOrderPaymentStatus(paymentRequestMessage.getOrderPaymentStatus())
                .setPaymentMethod(paymentRequestMessage.getPaymentMethod())
                .build();
    }

    /**
     * Chuyển đổi PaymentRequestMessageAvro thành PaymentRequestMessage
     */
    public PaymentRequestMessage paymentRequestMessageAvroToPaymentRequestMessage(
            PaymentRequestMessageAvro paymentRequestMessageAvro) {
        
        if (paymentRequestMessageAvro == null) {
            return null;
        }

        return PaymentRequestMessage.builder()
                .id(paymentRequestMessageAvro.getId().toString())
                .orderId(paymentRequestMessageAvro.getOrderId().toString())
                .amount(paymentRequestMessageAvro.getAmount().toString())
                .orderPaymentStatus(paymentRequestMessageAvro.getOrderPaymentStatus().toString())
                .paymentMethod(paymentRequestMessageAvro.getPaymentMethod().toString())
                .build();
    }

    /**
     * Chuyển đổi PaymentResponseMessage thành PaymentResponseMessageAvro
     */
    public PaymentResponseMessageAvro paymentResponseMessageToPaymentResponseMessageAvro(
            PaymentResponseMessage paymentResponseMessage) {
        
        if (paymentResponseMessage == null) {
            return null;
        }

        return PaymentResponseMessageAvro.newBuilder()
                .setId(paymentResponseMessage.getId())
                .setOrderId(paymentResponseMessage.getOrderId())
                .setOrderPaymentStatus(paymentResponseMessage.getOrderPaymentStatus())
                .setPaymentMethod(paymentResponseMessage.getPaymentMethod())
                .build();
    }

    /**
     * Chuyển đổi PaymentResponseMessageAvro thành PaymentResponseMessage
     */
    public PaymentResponseMessage paymentResponseMessageAvroToPaymentResponseMessage(
            PaymentResponseMessageAvro paymentResponseMessageAvro) {
        
        if (paymentResponseMessageAvro == null) {
            return null;
        }

        return PaymentResponseMessage.builder()
                .id(paymentResponseMessageAvro.getId().toString())
                .orderId(paymentResponseMessageAvro.getOrderId().toString())
                .orderPaymentStatus(paymentResponseMessageAvro.getOrderPaymentStatus().toString())
                .paymentMethod(paymentResponseMessageAvro.getPaymentMethod().toString())
                .build();
    }
}
