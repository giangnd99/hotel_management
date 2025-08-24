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

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentDepositResponseKafkaListener implements KafkaConsumer<BookingPaymentResponseAvro> {

    private final BookingMessageDataMapper bookingDataMapper;
    private final PaymentDepositListener paymentResponseKafkaListener;


    @Override
    @KafkaListener(topics = "${booking-service.payment-response-topic-name}", id = "${kafka-consumer-config.deposit-consumer-group-id}")
    public void receive(@Payload List<BookingPaymentResponseAvro> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        validateInputParameters(messages, keys, partitions, offsets);

        logBatchProcessingInfo(messages.size(), keys, partitions, offsets);

        processPaymentResponseMessages(messages);
    }

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


    private void processPaymentResponseMessages(List<BookingPaymentResponseAvro> messages) {
        messages.forEach(this::processSinglePaymentMessage);
    }


    private void processSinglePaymentMessage(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        try {
            logPaymentProcessingStart(bookingPaymentResponseAvro);

            processPaymentByStatus(bookingPaymentResponseAvro);

            logPaymentProcessingSuccess(bookingPaymentResponseAvro);

        } catch (OptimisticLockingFailureException e) {
            handleOptimisticLockingFailure(bookingPaymentResponseAvro, e);
        } catch (BookingDomainException e) {
            handleBookingDomainException(bookingPaymentResponseAvro, e);
        } catch (Exception e) {
            handleGenericException(bookingPaymentResponseAvro, e);
        }
    }

    private void processPaymentByStatus(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        PaymentStatus paymentStatus = bookingPaymentResponseAvro.getPaymentStatus();

        if (paymentStatus.compareTo(PaymentStatus.COMPLETED) == 0) {
            processCompletedPayment(bookingPaymentResponseAvro);
        } else if (paymentStatus.compareTo(PaymentStatus.FAILED) == 0 ||
                paymentStatus.compareTo(PaymentStatus.CANCELLED) == 0) {
            processFailedPayment(bookingPaymentResponseAvro);
        } else {
            handleUnknownPaymentStatus(bookingPaymentResponseAvro, paymentStatus);
        }
    }

    private void processCompletedPayment(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        log.info("Payment deposit completed cho booking id: {}", bookingPaymentResponseAvro.getBookingId());

        var paymentMessageResponse = bookingDataMapper.paymentResponseAvroToPayment(bookingPaymentResponseAvro);
        paymentResponseKafkaListener.paymentDepositCompleted(paymentMessageResponse);
    }

    private void processFailedPayment(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        log.info("Payment deposit failed cho booking id: {}", bookingPaymentResponseAvro.getBookingId());

        var paymentMessageResponse = bookingDataMapper.paymentResponseAvroToPayment(bookingPaymentResponseAvro);
        paymentResponseKafkaListener.paymentDepositCancelled(paymentMessageResponse);
    }

    private void handleUnknownPaymentStatus(BookingPaymentResponseAvro bookingPaymentResponseAvro,
                                            PaymentStatus paymentStatus) {
        log.warn("Unknown payment status: {} cho booking id: {}",
                paymentStatus,
                bookingPaymentResponseAvro.getBookingId());

    }

    private void logPaymentProcessingStart(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        log.debug("Bắt đầu xử lý payment response cho booking: {}, payment: {}, status: {}",
                bookingPaymentResponseAvro.getBookingId(),
                bookingPaymentResponseAvro.getPaymentId(),
                bookingPaymentResponseAvro.getPaymentStatus());
    }

    private void logPaymentProcessingSuccess(BookingPaymentResponseAvro bookingPaymentResponseAvro) {
        log.debug("Xử lý payment response thành công cho booking: {}, payment: {}",
                bookingPaymentResponseAvro.getBookingId(),
                bookingPaymentResponseAvro.getPaymentId());
    }

    private void handleOptimisticLockingFailure(BookingPaymentResponseAvro bookingPaymentResponseAvro,
                                                OptimisticLockingFailureException exception) {
        log.error("Optimistic locking failure cho booking id: {}. Lỗi: {}",
                bookingPaymentResponseAvro.getBookingId(),
                exception.getMessage());
  }

    private void handleBookingDomainException(BookingPaymentResponseAvro bookingPaymentResponseAvro,
                                              BookingDomainException exception) {
        log.error("Booking domain exception cho booking id: {}. Lỗi: {}",
                bookingPaymentResponseAvro.getBookingId(),
                exception.getMessage());

    }

    private void handleGenericException(BookingPaymentResponseAvro bookingPaymentResponseAvro,
                                        Exception exception) {
        log.error("Unexpected error khi xử lý payment response cho booking: {}. Lỗi: {}",
                bookingPaymentResponseAvro.getBookingId(),
                exception.getMessage(),
                exception);
    }
}
