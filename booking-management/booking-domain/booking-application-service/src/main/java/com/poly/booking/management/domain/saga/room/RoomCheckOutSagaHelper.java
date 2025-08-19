package com.poly.booking.management.domain.saga.room;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.CheckOutEvent;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.RoomOutboxService;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.BookingId;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Room Check Out Saga Helper - Hỗ trợ xử lý business logic checkout
 * <p>
 * CHỨC NĂNG:
 * - Validate và quản lý outbox messages cho checkout
 * - Thực hiện business logic checkout
 * - Quản lý saga status và outbox messages
 * <p>
 * MỤC ĐÍCH:
 * - Tách biệt business logic khỏi saga step
 * - Cung cấp các helper methods cho checkout process
 * - Đảm bảo tính nhất quán dữ liệu
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RoomCheckOutSagaHelper {

    private final RoomOutboxService roomOutboxService;
    private final BookingRepository bookingRepository;
    private final BookingDomainService bookingDomainService;

    /**
     * Validate và lấy outbox message cho checkout
     *
     * @param data Room message response
     * @return RoomOutboxMessage nếu tìm thấy, null nếu đã xử lý
     */
    public RoomOutboxMessage validateAndGetOutboxMessage(RoomMessageResponse data) {
        Optional<RoomOutboxMessage> outboxMessageOpt =
                roomOutboxService.getRoomOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.PROCESSING);
        if (outboxMessageOpt.isEmpty()) {
            log.error("Could not find room check out outbox message for saga id: {}", data.getSagaId());
            return null;
        }
        return outboxMessageOpt.get();
    }

    /**
     * Validate và lấy outbox message cho rollback
     *
     * @param data Room message response
     * @return RoomOutboxMessage nếu tìm thấy, null nếu không tìm thấy
     */
    public RoomOutboxMessage validateAndGetOutboxMessageForRollback(RoomMessageResponse data) {
        Optional<RoomOutboxMessage> outboxMessageOpt =
                roomOutboxService.getRoomOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.PROCESSING);
        if (outboxMessageOpt.isEmpty()) {
            log.error("Could not find room check out outbox message for rollback with saga id: {}", data.getSagaId());
            return null;
        }
        return outboxMessageOpt.get();
    }

    /**
     * Thực hiện business logic checkout
     *
     * @param outboxMessage Outbox message chứa thông tin booking
     * @return CheckOutEvent domain event
     */
    public CheckOutEvent executeRoomCheckOut(RoomOutboxMessage outboxMessage) {
        log.info("Executing room check out for booking: {}", outboxMessage.getBookingId());

        // Tìm booking entity
        Booking booking = findBooking(outboxMessage.getBookingId().toString());

        // Thực hiện business logic checkout
        CheckOutEvent domainEvent = bookingDomainService.checkOutBooking(booking);

        // Lưu trạng thái mới
        bookingRepository.save(booking);

        return domainEvent;
    }

    /**
     * Cập nhật saga status và lưu outbox message
     *
     * @param outboxMessage Outbox message cần cập nhật
     * @param domainEvent  Domain event chứa trạng thái mới
     */
    public void updateSagaStatusAndSaveOutbox(RoomOutboxMessage outboxMessage, CheckOutEvent domainEvent) {
        log.info("Updating saga status and outbox message for booking: {}", outboxMessage.getBookingId());

        // Cập nhật saga status
        outboxMessage.setSagaStatus(SagaStatus.FINISHED);
        outboxMessage.setOutboxStatus(com.poly.outbox.OutboxStatus.COMPLETED);

        // Lưu outbox message
        roomOutboxService.save(outboxMessage);

        log.info("Saga status and outbox message updated successfully for booking: {}", outboxMessage.getBookingId());
    }

    /**
     * Thực hiện rollback business logic
     *
     * @param data Room message response
     */
    public void executeRollbackBusinessLogic(RoomMessageResponse data) {
        log.info("Executing rollback business logic for booking: {}", data.getBookingId());

        // Tìm booking và revert status nếu cần
        Booking booking = findBooking(data.getBookingId());
        
        // TODO: Implement rollback logic based on business requirements
        // Có thể cần revert booking status từ CHECKED_OUT về trạng thái trước đó
        
        log.info("Rollback business logic completed for booking: {}", data.getBookingId());
    }

    /**
     * Cập nhật outbox message cho rollback
     *
     * @param outboxMessage Outbox message cần cập nhật
     * @param data          Room message response
     */
    public void updateOutboxMessageForRollback(RoomOutboxMessage outboxMessage, RoomMessageResponse data) {
        log.info("Updating outbox message for rollback with booking: {}", data.getBookingId());

        // Cập nhật saga status
        outboxMessage.setSagaStatus(SagaStatus.FAILED);
        outboxMessage.setOutboxStatus(com.poly.outbox.OutboxStatus.FAILED);

        // Lưu outbox message
        roomOutboxService.save(outboxMessage);

        log.info("Outbox message updated for rollback with booking: {}", data.getBookingId());
    }

    /**
     * Tìm booking theo ID
     *
     * @param bookingId Booking ID dạng string
     * @return Booking entity
     */
    private Booking findBooking(String bookingId) {
        return bookingRepository.findById(new BookingId(UUID.fromString(bookingId)))
                .orElseThrow(() -> new RuntimeException("Booking not found for check out: " + bookingId));
    }
}
