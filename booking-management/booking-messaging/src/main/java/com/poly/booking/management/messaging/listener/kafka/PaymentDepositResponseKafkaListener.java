package com.poly.booking.management.messaging.listener.kafka;

import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.kafka.model.BookingPaymentResponseAvro;
import com.poly.booking.management.domain.kafka.model.PaymentStatus;
import com.poly.booking.management.domain.port.in.message.listener.PaymentDepositListener;
import com.poly.booking.management.messaging.mapper.BookingMessageDataMapper;
import com.poly.kafka.consumer.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Kafka Listener chịu trách nhiệm lắng nghe và xử lý các response message từ Payment Service
 * về việc thanh toán đặt cọc (deposit payment).
 * <p>
 * NGHIỆP VỤ:
 * - Nhận thông báo kết quả thanh toán đặt cọc từ Payment Service
 * - Xử lý các trạng thái thanh toán: COMPLETED, FAILED, CANCELLED
 * - Chuyển đổi Avro model thành domain entity
 * - Gửi thông tin payment đến PaymentDepositListener để xử lý business logic
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Event-Driven Architecture: Lắng nghe payment response events
 * - Message Mapping: Chuyển đổi giữa Avro và domain models
 * - Separation of Concerns: Tách biệt message handling và business logic
 * - Error Handling: Xử lý các loại exception khác nhau
 * <p>
 * FLOW XỬ LÝ:
 * 1. Nhận payment response message từ Kafka topic
 * 2. Log thông tin message để tracking
 * 3. Kiểm tra payment status (COMPLETED/FAILED/CANCELLED)
 * 4. Chuyển đổi BookingPaymentResponseAvro thành PaymentMessageResponse
 * 5. Gửi đến PaymentDepositListener để xử lý business logic
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentDepositResponseKafkaListener implements KafkaConsumer<BookingPaymentResponseAvro> {

    private final BookingMessageDataMapper bookingDataMapper;
    private final PaymentDepositListener paymentResponseKafkaListener;

    /**
     * Lắng nghe và xử lý payment deposit response messages từ Kafka topic
     * <p>
     * NGHIỆP VỤ:
     * - Nhận batch messages từ payment service
     * - Xử lý từng payment response event
     * - Đảm bảo reliable message processing với error handling
     *
     * @param messages   Danh sách BookingPaymentResponseAvro messages
     * @param keys       Kafka message keys
     * @param partitions Kafka partition numbers
     * @param offsets    Kafka message offsets
     */
    @Override
    @KafkaListener(topics = "${booking-service.payment-response-topic-name}", id = "${kafka-consumer-config.deposit-consumer-group-id}")
    public void receive(@Payload List<BookingPaymentResponseAvro> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        // Validate input parameters
        validateInputParameters(messages, keys, partitions, offsets);

        // Log thông tin batch processing
        logBatchProcessingInfo(messages.size(), keys, partitions, offsets);

        // Process each payment response message
        processPaymentResponseMessages(messages);
    }

    /**
     * Validate các tham số đầu vào
     * <p>
     * Đảm bảo tất cả thông tin cần thiết cho việc xử lý message đều hợp lệ
     */
    private void validateInputParameters(List<BookingPaymentResponseAvro> messages,
                                         List<String> keys,
                                         List<Integer> partitions,
                                         List<Long> offsets) {
        Assert.notNull(messages, "Messages list không được null");
        Assert.notNull(keys, "Keys list không được null");
        Assert.notNull(partitions, "Partitions list không được null");
        Assert.notNull(offsets, "Offsets list không được null");
        Assert.isTrue(messages.size() == keys.size(), "Messages và keys phải có cùng kích thước");
        Assert.isTrue(messages.size() == partitions.size(), "Messages và partitions phải có cùng kích thước");
        Assert.isTrue(messages.size() == offsets.size(), "Messages và offsets phải có cùng kích thước");
    }

    /**
     * Log thông tin batch processing
     * <p>
     * Ghi log chi tiết về việc xử lý batch messages để tracking và monitoring
     */
    private void logBatchProcessingInfo(int messageCount,
                                        List<String> keys,
                                        List<Integer> partitions,
                                        List<Long> offsets) {
        log.info("Nhận {} payment response messages với keys: {}, partitions: {} và offsets: {}",
                messageCount,
                keys.toString(),
                partitions.toString(),
                offsets.toString());
    }

    /**
     * Xử lý từng payment response message
     * <p>
     * Chuyển đổi Avro model thành domain entity và gửi đến business logic handler
     */
    private void processPaymentResponseMessages(List<BookingPaymentResponseAvro> messages) {
        messages.forEach(this::processSinglePaymentMessage);
    }

    /**
     * Xử lý một payment response message
     * <p>
     * Kiểm tra payment status và gửi đến handler phù hợp
     */
    private void processSinglePaymentMessage(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        try {
            // Log thông tin payment đang xử lý
            logPaymentProcessingStart(bookingPaymentResponseAvro);

            // Xử lý theo payment status
            processPaymentByStatus(bookingPaymentResponseAvro);

            // Log thành công
            logPaymentProcessingSuccess(bookingPaymentResponseAvro);

        } catch (OptimisticLockingFailureException e) {
            // Xử lý optimistic locking failure
            handleOptimisticLockingFailure(bookingPaymentResponseAvro, e);
        } catch (BookingDomainException e) {
            // Xử lý booking domain exception
            handleBookingDomainException(bookingPaymentResponseAvro, e);
        } catch (Exception e) {
            // Xử lý các exception khác
            handleGenericException(bookingPaymentResponseAvro, e);
        }
    }

    /**
     * Xử lý payment theo trạng thái
     * <p>
     * Chuyển đổi Avro model thành domain entity và gửi đến handler phù hợp
     */
    private void processPaymentByStatus(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        PaymentStatus paymentStatus = bookingPaymentResponseAvro.getPaymentStatus();

        if (paymentStatus.compareTo(PaymentStatus.COMPLETED) == 0) {
            // Xử lý payment thành công
            processCompletedPayment(bookingPaymentResponseAvro);
        } else if (paymentStatus.compareTo(PaymentStatus.FAILED) == 0 ||
                paymentStatus.compareTo(PaymentStatus.CANCELLED) == 0) {
            // Xử lý payment thất bại hoặc bị hủy
            processFailedPayment(bookingPaymentResponseAvro);
        } else {
            // Xử lý trạng thái không xác định
            handleUnknownPaymentStatus(bookingPaymentResponseAvro, paymentStatus);
        }
    }

    /**
     * Xử lý payment thành công
     */
    private void processCompletedPayment(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        log.info("Payment deposit completed cho booking id: {}", bookingPaymentResponseAvro.getBookingId());

        var paymentMessageResponse = bookingDataMapper.paymentResponseAvroToPayment(bookingPaymentResponseAvro);
        paymentResponseKafkaListener.paymentDepositCompleted(paymentMessageResponse);
    }

    /**
     * Xử lý payment thất bại hoặc bị hủy
     */
    private void processFailedPayment(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        log.info("Payment deposit failed cho booking id: {}", bookingPaymentResponseAvro.getBookingId());

        var paymentMessageResponse = bookingDataMapper.paymentResponseAvroToPayment(bookingPaymentResponseAvro);
        paymentResponseKafkaListener.paymentDepositCancelled(paymentMessageResponse);
    }

    /**
     * Xử lý trạng thái payment không xác định
     */
    private void handleUnknownPaymentStatus(BookingPaymentResponseAvro bookingPaymentResponseAvro,
                                            PaymentStatus paymentStatus) {
        log.warn("Unknown payment status: {} cho booking id: {}",
                paymentStatus,
                bookingPaymentResponseAvro.getBookingId());

        // Có thể gửi đến dead letter queue hoặc xử lý đặc biệt
        // TODO: Implement dead letter queue handling
    }

    /**
     * Log thông tin bắt đầu xử lý payment
     */
    private void logPaymentProcessingStart(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        log.debug("Bắt đầu xử lý payment response cho booking: {}, payment: {}, status: {}",
                bookingPaymentResponseAvro.getBookingId(),
                bookingPaymentResponseAvro.getPaymentId(),
                bookingPaymentResponseAvro.getPaymentStatus());
    }

    /**
     * Log thông tin xử lý payment thành công
     */
    private void logPaymentProcessingSuccess(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        log.debug("Xử lý payment response thành công cho booking: {}, payment: {}",
                bookingPaymentResponseAvro.getBookingId(),
                bookingPaymentResponseAvro.getPaymentId());
    }

    /**
     * Xử lý OptimisticLockingFailureException
     * <p>
     * Xảy ra khi có conflict trong concurrent access
     */
    private void handleOptimisticLockingFailure(BookingPaymentResponseAvro bookingPaymentResponseAvro,
                                                OptimisticLockingFailureException exception) {
        log.error("Optimistic locking failure cho booking id: {}. Lỗi: {}",
                bookingPaymentResponseAvro.getBookingId(),
                exception.getMessage());

        // TODO: Có thể implement retry logic với exponential backoff
        // TODO: Có thể gửi message đến dead letter queue để xử lý sau
    }

    /**
     * Xử lý BookingDomainException
     * <p>
     * Xảy ra khi có lỗi trong business logic
     */
    private void handleBookingDomainException(BookingPaymentResponseAvro bookingPaymentResponseAvro,
                                              BookingDomainException exception) {
        log.error("Booking domain exception cho booking id: {}. Lỗi: {}",
                bookingPaymentResponseAvro.getBookingId(),
                exception.getMessage());

        // TODO: Có thể gửi notification đến admin
        // TODO: Có thể implement compensation logic
    }

    /**
     * Xử lý các exception khác
     * <p>
     * Xử lý các exception không mong đợi
     */
    private void handleGenericException(BookingPaymentResponseAvro bookingPaymentResponseAvro,
                                        Exception exception) {
        log.error("Unexpected error khi xử lý payment response cho booking: {}. Lỗi: {}",
                bookingPaymentResponseAvro.getBookingId(),
                exception.getMessage(),
                exception);

        // TODO: Có thể gửi notification đến admin
        // TODO: Có thể implement circuit breaker pattern
    }
}
