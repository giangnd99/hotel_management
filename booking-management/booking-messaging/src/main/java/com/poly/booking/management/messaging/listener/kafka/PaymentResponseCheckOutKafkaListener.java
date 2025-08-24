package com.poly.booking.management.messaging.listener.kafka;

import com.poly.booking.management.domain.kafka.model.BookingPaymentResponseAvro;
import com.poly.booking.management.domain.message.reponse.PaymentMessageResponse;
import com.poly.booking.management.domain.port.in.message.listener.PaymentCheckOutListener;
import com.poly.booking.management.messaging.mapper.BookingMessageDataMapper;
import com.poly.kafka.consumer.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResponseCheckOutKafkaListener implements KafkaConsumer<BookingPaymentResponseAvro> {

    private final PaymentCheckOutListener paymentCheckOutListener;
    private final BookingMessageDataMapper bookingDataMapper;

    @Override
    @KafkaListener(topics = "booking-payment-checkout-response",groupId = "room-check-out-group")
    public void receive(
            @Payload List<BookingPaymentResponseAvro> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("Received {} room check out messages from Kafka topic", messages.size());

        try {
            for (int i = 0; i < messages.size(); i++) {
                BookingPaymentResponseAvro message = messages.get(i);
                String key = keys.get(i);
                Integer partition = partitions.get(i);
                Long offset = offsets.get(i);

                processRoomCheckOutMessage(message, key, partition, offset);
            }

            log.info("Successfully processed {} room check out messages", messages.size());

        } catch (Exception e) {
            log.error("Error processing room check out messages", e);
        }
    }

    private void processRoomCheckOutMessage(BookingPaymentResponseAvro message, String key, Integer partition, Long offset) {
        try {
            log.debug("Processing room check out message - Key: {}, Partition: {}, Offset: {}", key, partition, offset);

            validateRoomCheckOutMessage(message);

            PaymentMessageResponse paymentMessageResponse = convertToRoomMessageResponse(message);

            processRoomCheckOutBusinessLogic(paymentMessageResponse);

            log.debug("Room check out message processed successfully - Key: {}, Partition: {}, Offset: {}",
                    key, partition, offset);

        } catch (Exception e) {
            log.error("Error processing room check out message - Key: {}, Partition: {}, Offset: {}. Error: {}",
                    key, partition, offset, e.getMessage(), e);
        }
    }

    private void validateRoomCheckOutMessage(BookingPaymentResponseAvro message) {
        if (message == null) {
            throw new IllegalArgumentException("Room check out message cannot be null");
        }

        if (message.getBookingId() == null || message.getBookingId().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        if (message.getSagaId() == null || message.getSagaId().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Saga ID cannot be null or empty");
        }

        log.debug("Room check out message validation passed for booking: {}", message.getBookingId());
    }


    private PaymentMessageResponse convertToRoomMessageResponse(BookingPaymentResponseAvro message) {
        return bookingDataMapper.paymentResponseAvroToPayment(message);
    }

    private void processRoomCheckOutBusinessLogic(PaymentMessageResponse roomMessageResponse) {
        paymentCheckOutListener.paymentResponseCheckOutCompleted(roomMessageResponse);
        log.info("Room check out completed for booking: {}", roomMessageResponse.getBookingId());
    }
}
