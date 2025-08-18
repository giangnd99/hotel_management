package com.poly.booking.management.messaging.listener.kafka;

import com.poly.booking.management.domain.exception.BookingDomainException;

import com.poly.booking.management.domain.kafka.model.BookingRoomResponseAvro;
import com.poly.booking.management.domain.port.in.message.listener.RoomReservedListener;
import com.poly.booking.management.messaging.mapper.BookingMessageDataMapper;
import com.poly.domain.valueobject.RoomResponseStatus;
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
 * Kafka Listener chịu trách nhiệm lắng nghe và xử lý các response message từ Room Service
 * về việc đặt phòng (room reservation).
 * 
 * NGHIỆP VỤ:
 * - Nhận thông báo kết quả đặt phòng từ Room Service
 * - Xử lý các trạng thái đặt phòng: SUCCESS, FAILED, CANCELLED
 * - Chuyển đổi Avro model thành domain entity
 * - Gửi thông tin room reservation đến RoomReservedListener để xử lý business logic
 * 
 * PATTERNS ÁP DỤNG:
 * - Event-Driven Architecture: Lắng nghe room reservation response events
 * - Message Mapping: Chuyển đổi giữa Avro và domain models
 * - Separation of Concerns: Tách biệt message handling và business logic
 * - Error Handling: Xử lý các loại exception khác nhau
 * 
 * FLOW XỬ LÝ:
 * 1. Nhận room reservation response message từ Kafka topic
 * 2. Log thông tin message để tracking
 * 3. Kiểm tra reservation status (SUCCESS/FAILED/CANCELLED)
 * 4. Chuyển đổi BookingRoomResponseAvro thành RoomMessageResponse
 * 5. Gửi đến RoomReservedListener để xử lý business logic
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoomApprovalResponseKafkaListener implements KafkaConsumer<BookingRoomResponseAvro> {

    private final BookingMessageDataMapper bookingDataMapper;
    private final RoomReservedListener roomReservedListener;

    /**
     * Lắng nghe và xử lý room reservation response messages từ Kafka topic
     * 
     * NGHIỆP VỤ:
     * - Nhận batch messages từ room service
     * - Xử lý từng room reservation response event
     * - Đảm bảo reliable message processing với error handling
     * 
     * @param messages Danh sách BookingRoomResponseAvro messages
     * @param keys Kafka message keys
     * @param partitions Kafka partition numbers
     * @param offsets Kafka message offsets
     */
    @Override
    @KafkaListener(topics = "${booking-service.room-reserve-response-topic-name}", groupId = "${kafka-consumer-config.room-consumer-group-id}")
    public void receive(@Payload List<BookingRoomResponseAvro> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        
        // Validate input parameters
        validateInputParameters(messages, keys, partitions, offsets);
        
        // Log thông tin batch processing
        logBatchProcessingInfo(messages.size(), keys, partitions, offsets);
        
        // Process each room reservation response message
        processRoomReservationMessages(messages);
    }

    /**
     * Validate các tham số đầu vào
     * 
     * Đảm bảo tất cả thông tin cần thiết cho việc xử lý message đều hợp lệ
     */
    private void validateInputParameters(List<BookingRoomResponseAvro> messages,
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
     * 
     * Ghi log chi tiết về việc xử lý batch messages để tracking và monitoring
     */
    private void logBatchProcessingInfo(int messageCount,
                                      List<String> keys,
                                      List<Integer> partitions,
                                      List<Long> offsets) {
        log.info("Nhận {} room reservation response messages với keys: {}, partitions: {} và offsets: {}",
                messageCount,
                keys.toString(),
                partitions.toString(),
                offsets.toString());
    }

    /**
     * Xử lý từng room reservation response message
     * 
     * Chuyển đổi Avro model thành domain entity và gửi đến business logic handler
     */
    private void processRoomReservationMessages(List<BookingRoomResponseAvro> messages) {
        messages.forEach(this::processSingleRoomMessage);
    }

    /**
     * Xử lý một room reservation response message
     * 
     * Kiểm tra reservation status và gửi đến handler phù hợp
     */
    private void processSingleRoomMessage(BookingRoomResponseAvro bookingRoomResponseAvro) {
        try {
            // Log thông tin room reservation đang xử lý
            logRoomProcessingStart(bookingRoomResponseAvro);
            
            // Xử lý theo reservation status
            processRoomByStatus(bookingRoomResponseAvro);
            
            // Log thành công
            logRoomProcessingSuccess(bookingRoomResponseAvro);
            
        } catch (OptimisticLockingFailureException e) {
            // Xử lý optimistic locking failure
            handleOptimisticLockingFailure(bookingRoomResponseAvro, e);
        } catch (BookingDomainException e) {
            // Xử lý booking domain exception
            handleBookingDomainException(bookingRoomResponseAvro, e);
        } catch (Exception e) {
            // Xử lý các exception khác
            handleGenericException(bookingRoomResponseAvro, e);
        }
    }

    /**
     * Xử lý room reservation theo trạng thái
     * 
     * Chuyển đổi Avro model thành domain entity và gửi đến handler phù hợp
     */
    private void processRoomByStatus(BookingRoomResponseAvro bookingRoomResponseAvro) {
        String reservationStatus = bookingRoomResponseAvro.getReservationStatus();
        
        if (RoomResponseStatus.SUCCESS.name().equals(reservationStatus)) {
            // Xử lý room reservation thành công
            processSuccessfulRoomReservation(bookingRoomResponseAvro);
        } else if (RoomResponseStatus.FAILED.name().equals(reservationStatus) ||
                   RoomResponseStatus.CANCELLED.name().equals(reservationStatus)) {
            // Xử lý room reservation thất bại hoặc bị hủy
            processFailedRoomReservation(bookingRoomResponseAvro);
        } else {
            // Xử lý trạng thái không xác định
            handleUnknownReservationStatus(bookingRoomResponseAvro, reservationStatus);
        }
    }

    /**
     * Xử lý room reservation thành công
     */
    private void processSuccessfulRoomReservation(BookingRoomResponseAvro bookingRoomResponseAvro) {
        log.info("Room reservation approved cho booking id: {}", bookingRoomResponseAvro.getBookingId());
        
        var roomMessageResponse = bookingDataMapper.bookingRoomAvroToRoom(bookingRoomResponseAvro);
        roomReservedListener.roomReserved(roomMessageResponse);
    }

    /**
     * Xử lý room reservation thất bại hoặc bị hủy
     */
    private void processFailedRoomReservation(BookingRoomResponseAvro bookingRoomResponseAvro) {
        log.info("Room reservation failed cho booking id: {}", bookingRoomResponseAvro.getBookingId());
        
        // TODO: Implement room reservation failure handling
        // TODO: Có thể gửi notification cho customer về việc không thể đặt phòng
        // TODO: Có thể implement compensation logic
    }

    /**
     * Xử lý trạng thái reservation không xác định
     */
    private void handleUnknownReservationStatus(BookingRoomResponseAvro bookingRoomResponseAvro, 
                                              String reservationStatus) {
        log.warn("Unknown reservation status: {} cho booking id: {}", 
                reservationStatus, 
                bookingRoomResponseAvro.getBookingId());
        
        // Có thể gửi đến dead letter queue hoặc xử lý đặc biệt
        // TODO: Implement dead letter queue handling
    }

    /**
     * Log thông tin bắt đầu xử lý room reservation
     */
    private void logRoomProcessingStart(BookingRoomResponseAvro bookingRoomResponseAvro) {
        log.debug("Bắt đầu xử lý room reservation response cho booking: {}, status: {}",
                bookingRoomResponseAvro.getBookingId(),
                bookingRoomResponseAvro.getReservationStatus());
    }

    /**
     * Log thông tin xử lý room reservation thành công
     */
    private void logRoomProcessingSuccess(BookingRoomResponseAvro bookingRoomResponseAvro) {
        log.debug("Xử lý room reservation response thành công cho booking: {}",
                bookingRoomResponseAvro.getBookingId());
    }

    /**
     * Xử lý OptimisticLockingFailureException
     * 
     * Xảy ra khi có conflict trong concurrent access
     */
    private void handleOptimisticLockingFailure(BookingRoomResponseAvro bookingRoomResponseAvro, 
                                               OptimisticLockingFailureException exception) {
        log.error("Optimistic locking failure cho booking id: {}. Lỗi: {}",
                bookingRoomResponseAvro.getBookingId(),
                exception.getMessage());
        
        // TODO: Có thể implement retry logic với exponential backoff
        // TODO: Có thể gửi message đến dead letter queue để xử lý sau
    }

    /**
     * Xử lý BookingDomainException
     * 
     * Xảy ra khi có lỗi trong business logic
     */
    private void handleBookingDomainException(BookingRoomResponseAvro bookingRoomResponseAvro, 
                                             BookingDomainException exception) {
        log.error("Booking domain exception cho booking id: {}. Lỗi: {}",
                bookingRoomResponseAvro.getBookingId(),
                exception.getMessage());
        
        // TODO: Có thể gửi notification đến admin
        // TODO: Có thể implement compensation logic
    }

    /**
     * Xử lý các exception khác
     * 
     * Xử lý các exception không mong đợi
     */
    private void handleGenericException(BookingRoomResponseAvro bookingRoomResponseAvro, 
                                      Exception exception) {
        log.error("Unexpected error khi xử lý room reservation response cho booking: {}. Lỗi: {}",
                bookingRoomResponseAvro.getBookingId(),
                exception.getMessage(),
                exception);
        
        // TODO: Có thể gửi notification đến admin
        // TODO: Có thể implement circuit breaker pattern
    }
}
