package com.poly.booking.management.messaging.publisher.kafka;

import com.poly.booking.management.domain.config.BookingServiceConfigData;
import com.poly.booking.management.domain.kafka.model.BookingPaymentRequestAvro;
import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentEventPayload;
import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentOutboxMessage;
import com.poly.booking.management.domain.port.out.message.publisher.payment.PaymentRequestMessagePublisher;
import com.poly.booking.management.messaging.mapper.BookingMessageDataMapper;
import com.poly.kafka.producer.KafkaMessageHelper;
import com.poly.kafka.producer.service.KafkaProducer;
import com.poly.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.function.BiConsumer;

/**
 * Kafka Publisher chịu trách nhiệm gửi yêu cầu thanh toán tổng số tiền booking
 * đến Kafka topic để xử lý thanh toán cuối cùng.
 * 
 * Class này implement pattern Outbox để đảm bảo tính nhất quán dữ liệu
 * và khả năng retry khi gửi message thất bại.
 * 
 * Khác với DepositPaymentRequestKafkaPublisher, class này xử lý thanh toán
 * tổng số tiền thay vì chỉ đặt cọc.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestKafkaPublisher implements PaymentRequestMessagePublisher {

    private final BookingMessageDataMapper bookingDataMapper;
    private final KafkaProducer<String, BookingPaymentRequestAvro> kafkaProducer;
    private final BookingServiceConfigData bookingServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    /**
     * Gửi yêu cầu thanh toán tổng số tiền booking đến Kafka topic
     * 
     * @param paymentOutboxMessage Message chứa thông tin thanh toán từ outbox
     * @param outboxCallback Callback function để cập nhật trạng thái outbox
     */
    @Override
    public void sendTotalBookingAmount(BookingPaymentOutboxMessage paymentOutboxMessage, 
                                     BiConsumer<BookingPaymentOutboxMessage, OutboxStatus> outboxCallback) {
        
        // Validate input parameters
        validateInputParameters(paymentOutboxMessage, outboxCallback);
        
        // Extract và parse thông tin từ outbox message
        BookingPaymentEventPayload paymentEventPayload = extractPaymentEventPayload(paymentOutboxMessage);
        String sagaId = extractSagaId(paymentOutboxMessage);
        
        // Log thông tin bắt đầu xử lý
        logProcessingStart(paymentEventPayload, sagaId);
        
        try {
            // Tạo Avro model từ payment event
            BookingPaymentRequestAvro paymentRequestAvro = createPaymentRequestAvro(sagaId, paymentEventPayload);
            
            // Gửi message đến Kafka
            sendMessageToKafka(paymentRequestAvro, sagaId, paymentOutboxMessage, outboxCallback, paymentEventPayload);
            
            // Log thành công
            logProcessingSuccess(paymentEventPayload, sagaId);
            
        } catch (Exception e) {
            // Log và xử lý lỗi
            handleProcessingError(paymentEventPayload, sagaId, e);
        }
    }

    /**
     * Validate các tham số đầu vào
     */
    private void validateInputParameters(BookingPaymentOutboxMessage paymentOutboxMessage, 
                                       BiConsumer<BookingPaymentOutboxMessage, OutboxStatus> outboxCallback) {
        Assert.notNull(paymentOutboxMessage, "PaymentOutboxMessage không được null");
        Assert.notNull(outboxCallback, "OutboxCallback không được null");
        Assert.notNull(paymentOutboxMessage.getPayload(), "PaymentOutboxMessage payload không được null");
        Assert.notNull(paymentOutboxMessage.getSagaId(), "PaymentOutboxMessage sagaId không được null");
    }

    /**
     * Trích xuất và parse BookingPaymentEventPayload từ outbox message
     */
    private BookingPaymentEventPayload extractPaymentEventPayload(BookingPaymentOutboxMessage paymentOutboxMessage) {
        return kafkaMessageHelper.getEventPayload(
                paymentOutboxMessage.getPayload(),
                BookingPaymentEventPayload.class
        );
    }

    /**
     * Trích xuất Saga ID từ outbox message
     */
    private String extractSagaId(BookingPaymentOutboxMessage paymentOutboxMessage) {
        return paymentOutboxMessage.getSagaId().toString();
    }

    /**
     * Log thông tin bắt đầu xử lý payment request cho tổng số tiền
     */
    private void logProcessingStart(BookingPaymentEventPayload paymentEventPayload, String sagaId) {
        log.info("Bắt đầu xử lý TotalBookingAmountPaymentRequest cho booking id: {} và saga id: {}",
                paymentEventPayload.getBookingId(), sagaId);
    }

    /**
     * Tạo BookingPaymentRequestAvro model từ thông tin payment
     */
    private BookingPaymentRequestAvro createPaymentRequestAvro(String sagaId, 
                                                             BookingPaymentEventPayload paymentEventPayload) {
        return bookingDataMapper.bookingPaymentEventToPaymentRequestAvroModel(sagaId, paymentEventPayload);
    }

    /**
     * Gửi message đến Kafka topic
     */
    private void sendMessageToKafka(BookingPaymentRequestAvro paymentRequestAvro,
                                  String sagaId,
                                  BookingPaymentOutboxMessage paymentOutboxMessage,
                                  BiConsumer<BookingPaymentOutboxMessage, OutboxStatus> outboxCallback,
                                  BookingPaymentEventPayload paymentEventPayload) {
        
        String topicName = bookingServiceConfigData.getPaymentRequestTopicName();
        
        kafkaProducer.send(
                topicName,
                sagaId,
                paymentRequestAvro,
                kafkaMessageHelper.getKafkaCallback(
                        topicName,
                        paymentRequestAvro,
                        paymentOutboxMessage,
                        outboxCallback,
                        paymentEventPayload.getBookingId(),
                        "TotalBookingAmountPaymentRequestAvroModel"
                )
        );
    }

    /**
     * Log thông tin xử lý thành công
     */
    private void logProcessingSuccess(BookingPaymentEventPayload paymentEventPayload, String sagaId) {
        log.info("TotalBookingAmountPaymentRequest đã được gửi thành công đến Kafka cho booking id: {} và saga id: {}",
                paymentEventPayload.getBookingId(), sagaId);
    }

    /**
     * Xử lý lỗi khi gửi message
     */
    private void handleProcessingError(BookingPaymentEventPayload paymentEventPayload, 
                                     String sagaId, 
                                     Exception exception) {
        log.error("Lỗi khi gửi TotalBookingAmountPaymentRequest đến Kafka với booking id: {} và saga id: {}. Lỗi: {}",
                paymentEventPayload.getBookingId(), 
                sagaId, 
                exception.getMessage(), 
                exception);
        
        // Có thể thêm logic retry hoặc dead letter queue ở đây
        // throw new PaymentRequestPublishingException("Không thể gửi total booking amount payment request", exception);
    }
}
