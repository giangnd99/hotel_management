package com.poly.booking.management.domain.saga.payment;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingDepositedEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.RoomDataMapper;
import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.NotificationOutboxService;
import com.poly.booking.management.domain.outbox.service.impl.PaymentOutboxImpl;
import com.poly.booking.management.domain.outbox.service.impl.RoomOutboxServiceImpl;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.booking.management.domain.message.reponse.PaymentMessageResponse;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepositSagaHelper {
    // Business logic services
    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;
    private final BookingSagaHelper bookingSagaHelper;
    private final RoomDataMapper roomDataMapper;

    // Outbox pattern helpers for reliable messaging
    private final PaymentOutboxImpl paymentOutboxHelper;
    private final RoomOutboxServiceImpl roomOutboxServiceImpl;
    private final NotificationOutboxService notificationOutboxService;
    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Validate và lấy payment outbox message để tránh duplicate processing
     *
     * @param paymentResponse PaymentMessageResponse từ external payment service
     * @return BookingPaymentOutboxMessage nếu hợp lệ, null nếu đã processed
     */
    public PaymentOutboxMessage validateAndGetPaymentOutboxMessage(PaymentMessageResponse paymentResponse) {
        Optional<PaymentOutboxMessage> outboxMessageOpt =
                paymentOutboxHelper.getBySagaIdAndSagaStatus(
                        UUID.fromString(paymentResponse.getSagaId()),
                        SagaStatus.STARTED);

        if (outboxMessageOpt.isEmpty()) {
            log.info("Payment outbox message with saga id: {} already processed!",
                    paymentResponse.getSagaId());
            return null;
        }

        return outboxMessageOpt.get();
    }

    /**
     * Thực hiện business logic hoàn tất thanh toán
     *
     * @param paymentResponse PaymentMessageResponse từ external service
     * @return BookingPaidEvent domain event
     */
    public BookingDepositedEvent executePaymentCompletion(PaymentMessageResponse paymentResponse) {
        log.info("Executing payment completion for booking: {}", paymentResponse.getBookingId());

        // Thực hiện business logic thanh toán thông qua saga helper
        return completePaymentForBooking(paymentResponse);
    }

    /**
     * Cập nhật payment outbox message với trạng thái mới
     *
     * @param outboxMessage Payment outbox message cần cập nhật
     * @param domainEvent   Domain event chứa trạng thái mới
     */
    public void updatePaymentOutboxMessage(PaymentOutboxMessage outboxMessage,
                                           BookingDepositedEvent domainEvent) {
        // Chuyển đổi booking status sang saga status
        SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(
                domainEvent.getBooking().getStatus());

        // Tạo payment outbox message mới với trạng thái đã cập nhật
        PaymentOutboxMessage updatedOutboxMessage =
                createUpdatedPaymentOutboxMessage(outboxMessage,
                        domainEvent.getBooking().getStatus(),
                        sagaStatus);

        // Lưu payment outbox message
        paymentOutboxHelper.save(updatedOutboxMessage);
    }

    /**
     * Trigger bước tiếp theo - room reservation
     *
     * @param domainEvent Domain event chứa thông tin booking
     * @param sagaId      ID của saga
     */
    public void triggerRoomReservationStep(BookingDepositedEvent domainEvent, String sagaId) {
        log.info("Triggering room reservation step for booking: {}",
                domainEvent.getBooking().getId().getValue());

        // Tạo room outbox message để trigger room reservation step
        roomOutboxServiceImpl.saveRoomOutboxMessage(
                roomDataMapper.bookingDepositedEventToBookingReservedEventPayload(domainEvent),
                domainEvent.getBooking().getStatus(),
                bookingSagaHelper.bookingStatusToSagaStatus(domainEvent.getBooking().getStatus()),
                OutboxStatus.STARTED,
                UUID.fromString(sagaId));
    }

    /**
     * Tìm payment outbox message phù hợp để rollback
     *
     * @param paymentResponse PaymentMessageResponse chứa thông tin rollback
     * @return BookingPaymentOutboxMessage nếu tìm thấy, null nếu đã rollback
     */
    public PaymentOutboxMessage findPaymentOutboxMessageForRollback(PaymentMessageResponse paymentResponse) {
        SagaStatus[] validStatuses = getValidSagaStatusesForPaymentRollback(paymentResponse.getPaymentStatus());

        Optional<PaymentOutboxMessage> outboxMessageOpt =
                paymentOutboxHelper.getBySagaIdAndSagaStatus(
                        UUID.fromString(paymentResponse.getSagaId()),
                        validStatuses);

        if (outboxMessageOpt.isEmpty()) {
            log.info("Payment outbox message with saga id: {} already rolled back!",
                    paymentResponse.getSagaId());
            return null;
        }

        return outboxMessageOpt.get();
    }

    /**
     * Thực hiện rollback business logic cho payment
     *
     * @param paymentResponse PaymentMessageResponse chứa thông tin rollback
     * @return Booking entity sau khi rollback
     */
    public Booking executePaymentRollback(PaymentMessageResponse paymentResponse) {
        log.info("Executing payment rollback for booking: {}", paymentResponse.getBookingId());

        // Tìm booking entity
        Booking booking = bookingSagaHelper.findBooking(paymentResponse.getBookingId());

        // Thực hiện cancel booking
        bookingDomainService.cancelBooking(booking);

        // Lưu trạng thái đã cancel
        bookingRepository.save(booking);

        return booking;
    }

    /**
     * Cập nhật payment outbox message với trạng thái rollback
     *
     * @param outboxMessage Payment outbox message cần cập nhật
     * @param booking       Booking entity sau khi rollback
     */
    public void updatePaymentOutboxMessageForRollback(PaymentOutboxMessage outboxMessage,
                                                      Booking booking) {
        SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(booking.getStatus());

        PaymentOutboxMessage updatedOutboxMessage =
                createUpdatedPaymentOutboxMessage(outboxMessage, booking.getStatus(), sagaStatus);

        paymentOutboxHelper.save(updatedOutboxMessage);
    }

    /**
     * Rollback room reservation nếu payment bị cancel
     *
     * @param sagaId  ID của saga
     * @param booking Booking entity sau khi rollback
     */
    public void rollbackRoomReservation(String sagaId, Booking booking) {
        log.info("Rolling back room reservation for saga id: {}", sagaId);

        try {
            RoomOutboxMessage roomOutboxMessage =
                    findRoomOutboxMessageForRollback(sagaId);

            if (roomOutboxMessage != null) {
                SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(booking.getStatus());
                updateRoomOutboxMessageForRollback(roomOutboxMessage, booking.getStatus(), sagaStatus);
            }
        } catch (Exception e) {
            log.warn("Failed to rollback room reservation for saga id: {}", sagaId, e);
        }
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Xác định các saga status hợp lệ cho payment rollback dựa trên payment status
     * <p>
     * LOGIC:
     * - COMPLETED: Chỉ rollback khi đang STARTED (payment đã hoàn tất nhưng cần rollback)
     * - CANCELLED: Rollback từ PROCESSING (payment đang xử lý bị cancel)
     * - FAILED: Rollback từ STARTED hoặc PROCESSING (payment thất bại ở bất kỳ giai đoạn nào)
     *
     * @param paymentStatus Trạng thái thanh toán từ external payment service
     * @return Array các SagaStatus hợp lệ cho rollback
     */
    public SagaStatus[] getValidSagaStatusesForPaymentRollback(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case PAID -> new SagaStatus[]{SagaStatus.STARTED};
            case CANCELED -> new SagaStatus[]{SagaStatus.PROCESSING};
            case FAILED, PENDING, EXPIRED -> new SagaStatus[]{SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }

    /**
     * Tạo payment outbox message đã cập nhật
     *
     * @param outboxMessage Payment outbox message gốc
     * @param bookingStatus Trạng thái booking mới
     * @param sagaStatus    Trạng thái saga mới
     * @return BookingPaymentOutboxMessage đã cập nhật
     */
    public PaymentOutboxMessage createUpdatedPaymentOutboxMessage(
            PaymentOutboxMessage outboxMessage,
            BookingStatus bookingStatus,
            SagaStatus sagaStatus) {

        outboxMessage.setProcessedAt(DateCustom.now().getValue());
        outboxMessage.setBookingStatus(bookingStatus);
        outboxMessage.setSagaStatus(sagaStatus);

        return outboxMessage;
    }

    /**
     * Tìm room outbox message để rollback
     *
     * @param sagaId ID của saga
     * @return BookingRoomOutboxMessage nếu tìm thấy, null nếu không tìm thấy
     */
    public RoomOutboxMessage findRoomOutboxMessageForRollback(String sagaId) {
        Optional<RoomOutboxMessage> roomOutboxMessageOpt =
                roomOutboxServiceImpl.getRoomOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(sagaId),
                        SagaStatus.COMPENSATING);

        if (roomOutboxMessageOpt.isEmpty()) {
            throw new BookingDomainException(
                    "Room outbox message with saga id: " + sagaId +
                            " is not found in " + SagaStatus.COMPENSATING.name() + " status!");
        }

        return roomOutboxMessageOpt.get();
    }

    /**
     * Cập nhật room outbox message với trạng thái rollback
     *
     * @param roomOutboxMessage Room outbox message cần cập nhật
     * @param bookingStatus     Trạng thái booking mới
     * @param sagaStatus        Trạng thái saga mới
     */
    public void updateRoomOutboxMessageForRollback(RoomOutboxMessage roomOutboxMessage,
                                                   BookingStatus bookingStatus,
                                                   SagaStatus sagaStatus) {
        roomOutboxMessage.setBookingStatus(bookingStatus);
        roomOutboxMessage.setSagaStatus(sagaStatus);
        roomOutboxMessage.setProcessedAt(LocalDateTime.now());

        roomOutboxServiceImpl.save(roomOutboxMessage);
    }

    /**
     * Thực hiện business logic hoàn tất thanh toán
     *
     * @param data PaymentMessageResponse chứa thông tin thanh toán
     * @return BookingPaidEvent domain event
     */
    public BookingDepositedEvent completePaymentForBooking(PaymentMessageResponse data) {
        log.info("Completing payment for booking with id: {}", data.getBookingId());
        Booking booking = bookingSagaHelper.findBooking(data.getBookingId());
        BookingDepositedEvent domainEvent = bookingDomainService.depositBooking(booking);
        bookingRepository.save(booking);
        return domainEvent;
    }
}
