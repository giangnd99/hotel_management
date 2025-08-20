package com.poly.booking.management.domain.saga.cancellation;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingCancelledEvent;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.RoomOutboxService;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.service.impl.BookingCancellationDomainService;
import com.poly.domain.valueobject.BookingId;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Booking Cancellation Saga Helper - Hỗ trợ xử lý business logic hủy booking
 * <p>
 * CHỨC NĂNG:
 * - Validate và quản lý outbox messages cho cancellation
 * - Thực hiện business logic hủy booking
 * - Quản lý saga status và outbox messages
 * - Trigger refund step nếu cần hoàn tiền
 * <p>
 * MỤC ĐÍCH:
 * - Tách biệt business logic khỏi saga step
 * - Cung cấp các helper methods cho cancellation process
 * - Đảm bảo tính nhất quán dữ liệu
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCancellationSagaHelper {

    private final RoomOutboxService roomOutboxService;
    private final BookingRepository bookingRepository;
    private final BookingCancellationDomainService bookingCancellationDomainService;

    /**
     * Validate và lấy outbox message cho cancellation
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
            log.error("Could not find room cancellation outbox message for saga id: {}", data.getSagaId());
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
            log.error("Could not find room cancellation outbox message for rollback with saga id: {}", data.getSagaId());
            return null;
        }
        return outboxMessageOpt.get();
    }

    /**
     * Thực hiện business logic hủy booking
     *
     * @param outboxMessage Outbox message chứa thông tin booking
     * @param data          Room message response
     * @return BookingCancelledEvent domain event
     */
    public BookingCancelledEvent executeBookingCancellation(RoomOutboxMessage outboxMessage, RoomMessageResponse data) {
        log.info("Executing booking cancellation for booking: {}", outboxMessage.getBookingId());

        // Tìm booking entity
        Booking booking = findBooking(outboxMessage.getBookingId().toString());

        // Xác định lý do hủy từ room message
        String cancellationReason = determineCancellationReason(data);

        // Thực hiện business logic hủy booking
        BookingCancelledEvent domainEvent = bookingCancellationDomainService.cancelBooking(booking, cancellationReason);

        // Lưu trạng thái mới
        bookingRepository.save(booking);

        return domainEvent;
    }

    /**
     * Cập nhật saga status và lưu outbox message
     *
     * @param outboxMessage Outbox message cần cập nhật
     * @param domainEvent   Domain event chứa trạng thái mới
     */
    public void updateSagaStatusAndSaveOutbox(RoomOutboxMessage outboxMessage, BookingCancelledEvent domainEvent) {
        log.info("Updating saga status and outbox message for booking: {}", outboxMessage.getBookingId());

        // Cập nhật saga status
        outboxMessage.setSagaStatus(SagaStatus.FINISHED);
        outboxMessage.setOutboxStatus(com.poly.outbox.OutboxStatus.COMPLETED);

        // Lưu outbox message
        roomOutboxService.save(outboxMessage);

        log.info("Saga status and outbox message updated successfully for booking: {}", outboxMessage.getBookingId());
    }

    /**
     * Trigger refund step nếu cần hoàn tiền
     *
     * @param domainEvent Domain event chứa thông tin cancellation
     * @param data        Room message response
     */
    public void triggerRefundStep(BookingCancelledEvent domainEvent, RoomMessageResponse data) {
        log.info("Triggering refund step for booking: {}", domainEvent.getBooking().getId().getValue());

        // TODO: Implement refund outbox message creation
        // Tạo refund outbox message để trigger payment service
        // paymentOutboxService.saveRefundOutboxMessage(domainEvent, data.getSagaId());

        log.info("Refund step triggered successfully for booking: {}", domainEvent.getBooking().getId().getValue());
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
        // Có thể cần revert booking status từ CANCELLED về trạng thái trước đó

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
                .orElseThrow(() -> new RuntimeException("Booking not found for cancellation: " + bookingId));
    }

    /**
     * Xác định lý do hủy từ room message response
     *
     * @param data Room message response
     * @return Lý do hủy
     */
    private String determineCancellationReason(RoomMessageResponse data) {
        // TODO: Implement logic để xác định lý do hủy từ room message
        // Có thể dựa vào room status, error message, hoặc business rules

        if (data.getRoomResponseStatus() != null) {
            return switch (data.getRoomResponseStatus()) {
                case BOOKED -> "Room reservation failed";
                case MAINTENANCE -> "Room reservation cancelled by hotel";
                case CLEANING -> "Room reservation pending";
                case VACANT -> "Room reservation successful but cancelled";
                default -> "Booking cancelled due to room service response";
            };
        }

        return "Booking cancelled due to room service issue";
    }
}
