package com.poly.booking.management.domain.saga.checkin;

import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.RoomOutboxService;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.service.impl.BookingCheckInDomainService;
import com.poly.domain.valueobject.BookingId;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Booking Check-In Saga Step - Xử lý business logic check-in trong saga pattern
 * <p>
 * CHỨC NĂNG:
 * - Thực hiện business logic check-in booking
 * - Quản lý outbox messages cho check-in process
 * - Đảm bảo tính nhất quán dữ liệu trong saga
 * <p>
 * MỤC ĐÍCH:
 * - Xử lý check-in thành công, thất bại và đang chờ
 * - Cập nhật trạng thái booking và outbox messages
 * - Trigger các bước tiếp theo trong saga nếu cần
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingRoomCheckInStep {

    private final BookingCheckInDomainService bookingCheckInDomainService;
    private final BookingRepository bookingRepository;
    private final RoomOutboxService roomOutboxService;

    /**
     * Xử lý check-in thành công
     * <p>
     * BUSINESS LOGIC:
     * - Cập nhật trạng thái booking thành CHECKED_IN
     * - Ghi log check-in thành công
     * - Cập nhật outbox message status
     *
     * @param notificationMessageResponse Thông tin check-in từ notification service
     */
    public void process(NotificationMessageResponse notificationMessageResponse) {
        log.info("Processing successful check-in for booking: {}", notificationMessageResponse.getBookingId());

        try {
            // Tìm booking entity
            BookingId bookingId = new BookingId(UUID.fromString(notificationMessageResponse.getBookingId()));
            Optional<com.poly.booking.management.domain.entity.Booking> bookingOpt = 
                    bookingRepository.findById(bookingId.getValue());

            if (bookingOpt.isEmpty()) {
                log.error("Booking not found for check-in: {}", notificationMessageResponse.getBookingId());
                throw new RuntimeException("Booking not found: " + notificationMessageResponse.getBookingId());
            }

            com.poly.booking.management.domain.entity.Booking booking = bookingOpt.get();

            // Thực hiện business logic check-in
            bookingCheckInDomainService.checkInBooking(booking, notificationMessageResponse);

            // Lưu trạng thái mới
            bookingRepository.save(booking);

            // Cập nhật outbox message nếu có
            updateOutboxMessageForCheckIn(notificationMessageResponse);

            log.info("Check-in processed successfully for booking: {}", notificationMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing check-in for booking: {}", notificationMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process check-in", e);
        }
    }

    /**
     * Xử lý check-in thất bại
     * <p>
     * BUSINESS LOGIC:
     * - Ghi log check-in thất bại
     * - Cập nhật trạng thái booking nếu cần
     * - Cập nhật outbox message status
     *
     * @param notificationMessageResponse Thông tin check-in thất bại
     */
    public void processFailed(NotificationMessageResponse notificationMessageResponse) {
        log.info("Processing failed check-in for booking: {}", notificationMessageResponse.getBookingId());

        try {
            // Ghi log thất bại
            log.warn("Check-in failed for booking: {} with reason: {}", 
                    notificationMessageResponse.getBookingId(), 
                    notificationMessageResponse.getFailureMessages());

            // Cập nhật outbox message cho thất bại
            updateOutboxMessageForFailedCheckIn(notificationMessageResponse);

            log.info("Failed check-in processed for booking: {}", notificationMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing failed check-in for booking: {}", 
                    notificationMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process failed check-in", e);
        }
    }

    /**
     * Xử lý check-in đang chờ
     * <p>
     * BUSINESS LOGIC:
     * - Ghi log check-in đang chờ
     * - Cập nhật trạng thái booking thành PENDING_CHECKIN
     * - Cập nhật outbox message status
     *
     * @param notificationMessageResponse Thông tin check-in đang chờ
     */
    public void processPending(NotificationMessageResponse notificationMessageResponse) {
        log.info("Processing pending check-in for booking: {}", notificationMessageResponse.getBookingId());

        try {
            // Tìm booking entity
            BookingId bookingId = new BookingId(UUID.fromString(notificationMessageResponse.getBookingId()));
            Optional<com.poly.booking.management.domain.entity.Booking> bookingOpt = 
                    bookingRepository.findById(bookingId.getValue());

            if (bookingOpt.isEmpty()) {
                log.error("Booking not found for pending check-in: {}", notificationMessageResponse.getBookingId());
                throw new RuntimeException("Booking not found: " + notificationMessageResponse.getBookingId());
            }

            com.poly.booking.management.domain.entity.Booking booking = bookingOpt.get();

            // Cập nhật trạng thái thành PENDING_CHECKIN
            bookingCheckInDomainService.setPendingCheckIn(booking, notificationMessageResponse);

            // Lưu trạng thái mới
            bookingRepository.save(booking);

            // Cập nhật outbox message cho pending
            updateOutboxMessageForPendingCheckIn(notificationMessageResponse);

            log.info("Pending check-in processed for booking: {}", notificationMessageResponse.getBookingId());

        } catch (Exception e) {
            log.error("Error processing pending check-in for booking: {}", 
                    notificationMessageResponse.getBookingId(), e);
            throw new RuntimeException("Failed to process pending check-in", e);
        }
    }

    /**
     * Cập nhật outbox message cho check-in thành công
     */
    private void updateOutboxMessageForCheckIn(NotificationMessageResponse notificationMessageResponse) {
        try {
            Optional<RoomOutboxMessage> outboxMessageOpt = 
                    roomOutboxService.getRoomOutboxMessageBySagaIdAndSagaStatus(
                            UUID.fromString(notificationMessageResponse.getId()), 
                            SagaStatus.STARTED);

            if (outboxMessageOpt.isPresent()) {
                RoomOutboxMessage outboxMessage = outboxMessageOpt.get();
                outboxMessage.setSagaStatus(SagaStatus.FINISHED);
                outboxMessage.setOutboxStatus(com.poly.outbox.OutboxStatus.COMPLETED);
                roomOutboxService.save(outboxMessage);
                
                log.info("Outbox message updated for successful check-in: {}", 
                        notificationMessageResponse.getBookingId());
            }
        } catch (Exception e) {
            log.warn("Could not update outbox message for check-in: {}", 
                    notificationMessageResponse.getBookingId(), e);
        }
    }

    /**
     * Cập nhật outbox message cho check-in thất bại
     */
    private void updateOutboxMessageForFailedCheckIn(NotificationMessageResponse notificationMessageResponse) {
        try {
            Optional<RoomOutboxMessage> outboxMessageOpt = 
                    roomOutboxService.getRoomOutboxMessageBySagaIdAndSagaStatus(
                            UUID.fromString(notificationMessageResponse.getId()), 
                            SagaStatus.STARTED);

            if (outboxMessageOpt.isPresent()) {
                RoomOutboxMessage outboxMessage = outboxMessageOpt.get();
                outboxMessage.setSagaStatus(SagaStatus.FAILED);
                outboxMessage.setOutboxStatus(com.poly.outbox.OutboxStatus.FAILED);
                roomOutboxService.save(outboxMessage);
                
                log.info("Outbox message updated for failed check-in: {}", 
                        notificationMessageResponse.getBookingId());
            }
        } catch (Exception e) {
            log.warn("Could not update outbox message for failed check-in: {}", 
                    notificationMessageResponse.getBookingId(), e);
        }
    }

    /**
     * Cập nhật outbox message cho check-in đang chờ
     */
    private void updateOutboxMessageForPendingCheckIn(NotificationMessageResponse notificationMessageResponse) {
        try {
            Optional<RoomOutboxMessage> outboxMessageOpt = 
                    roomOutboxService.getRoomOutboxMessageBySagaIdAndSagaStatus(
                            UUID.fromString(notificationMessageResponse.getId()), 
                            SagaStatus.STARTED);

            if (outboxMessageOpt.isPresent()) {
                RoomOutboxMessage outboxMessage = outboxMessageOpt.get();
                outboxMessage.setSagaStatus(SagaStatus.PROCESSING);
                outboxMessage.setOutboxStatus(com.poly.outbox.OutboxStatus.STARTED);
                roomOutboxService.save(outboxMessage);
                
                log.info("Outbox message updated for pending check-in: {}", 
                        notificationMessageResponse.getBookingId());
            }
        } catch (Exception e) {
            log.warn("Could not update outbox message for pending check-in: {}", 
                    notificationMessageResponse.getBookingId(), e);
        }
    }
}
