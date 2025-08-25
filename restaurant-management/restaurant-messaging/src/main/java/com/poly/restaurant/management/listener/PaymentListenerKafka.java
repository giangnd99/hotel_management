//package com.poly.restaurant.management.listener;
//
//import com.poly.config.KafkaTopicsConfig;
//import com.poly.consumer.AbstractConsumer;
//import com.poly.dedup.DedupStore;
//import com.poly.domain.valueobject.PaymentStatus;
//import com.poly.message.BaseResponse;
//import com.poly.message.ResponseStatus;
//import com.poly.message.model.payment.PaymentResponseMessage;
//import com.poly.restaurant.application.port.in.message.listener.PaymentResponseListener;
//import com.poly.restaurant.domain.entity.Order;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@Slf4j
//public class PaymentListenerKafka extends AbstractConsumer<PaymentResponseMessage, BaseResponse> {
//
//    private final PaymentResponseListener paymentResponseListener;
//
//    protected PaymentListenerKafka(DedupStore dedup,
//                                   long dedupTtlMs,
//                                   long responseCacheTtlMs,
//                                   PaymentResponseListener paymentResponseListener) {
//        super(dedup, dedupTtlMs, responseCacheTtlMs);
//        this.paymentResponseListener = paymentResponseListener;
//    }
//
//    @Override
//    protected String idempotencyKey(PaymentResponseMessage msg) {
//        return msg.getSourceService() + msg.getCorrelationId();
//    }
//
//    @Override
//    protected BaseResponse handleBusiness(PaymentResponseMessage request) {
//
//        log.info("Received payment response for order {}. Status: {}", request.getCorrelationId(), request.getStatus());
//
//        if (request.getStatus() == ResponseStatus.OK) {
//            if (PaymentStatus.PAID == (PaymentStatus.valueOf(request.getPaymentStatus()))) {
//                paymentResponseListener.onPaymentSuccess(request);
//            } else {
//                log.warn("Payment failed for order {}. Reason: {}", request.getCorrelationId(), request.getErrorMessage());
//                paymentResponseListener.onPaymentFailure(request);
//            }
//        }
//        return null;
//    }
//
//    @KafkaListener(topics = KafkaTopicsConfig.PAYMENT_RESPONSE_TOPIC,
//            containerFactory = "jsonKafkaListenerContainerFactory")
//    public void listenPaymentResponse(@Payload List<PaymentResponseMessage> messages,
//                                      @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
//                                      @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
//                                      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
//        super.handleBatch(messages, keys, partitions, offsets);
//    }
//}
