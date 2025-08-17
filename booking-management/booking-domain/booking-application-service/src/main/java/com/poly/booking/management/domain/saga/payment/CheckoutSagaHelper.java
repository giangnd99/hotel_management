package com.poly.booking.management.domain.saga.payment;


import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.PaymentDataMapper;
import com.poly.booking.management.domain.mapper.RoomDataMapper;
import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.PaymentOutboxService;
import com.poly.booking.management.domain.outbox.service.RoomOutboxService;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.booking.management.domain.message.PaymentMessageResponse;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.PaymentStatus;
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
public class CheckoutSagaHelper {

    private final PaymentDataMapper paymentDataMapper;
    private final PaymentOutboxService paymentOutboxService;
    private final RoomOutboxService roomOutboxService;
    private final BookingSagaHelper bookingSagaHelper;
    private final BookingRepository bookingRepository;
    private final BookingDomainService bookingDomainService;
    private final RoomDataMapper roomDataMapper;

    //========================================== PRIVATE METHOD PROCESSING AND ROLLBACK ===========================
    public PaymentOutboxMessage validateAndGetPaymentOutboxMessage(PaymentMessageResponse paymentResponse) {
        Optional<PaymentOutboxMessage> outboxMessageOpt =
                paymentOutboxService.getBySagaIdAndSagaStatus(
                        UUID.fromString(paymentResponse.getSagaId()),
                        SagaStatus.PROCESSING);
        if (outboxMessageOpt.isEmpty()) {
            log.error("Could not find payment outbox message for saga id: {}", paymentResponse.getSagaId());
            return null;
        }
        return outboxMessageOpt.get();
    }

    public BookingPaidEvent executePaymentCompletion(PaymentMessageResponse paymentResponse) {

        log.info("Executing payment completion for booking: {}", paymentResponse.getBookingId());
        return completePaymentForBooking(paymentResponse);
    }

    public BookingPaidEvent completePaymentForBooking(PaymentMessageResponse paymentResponse) {
        log.info("Completing payment for booking with id: {}", paymentResponse.getBookingId());
        Booking booking = bookingSagaHelper.findBooking(paymentResponse.getBookingId());
        BookingPaidEvent domainEvent = bookingDomainService.payBooking(booking);
        bookingRepository.save(booking);
        return domainEvent;
    }

    public void updatePaymentOutboxMessage(PaymentOutboxMessage outboxMessage, BookingPaidEvent domainEvent) {
        // Chuyển đổi booking status sang saga status
        SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(
                domainEvent.getBooking().getStatus());

        // Tạo payment outbox message mới với trạng thái đã cập nhật
        PaymentOutboxMessage updatedOutboxMessage =
                createUpdatedPaymentOutboxMessage(outboxMessage,
                        domainEvent.getBooking().getStatus(),
                        sagaStatus);

        // Lưu payment outbox message
        paymentOutboxService.save(updatedOutboxMessage);
    }

    public void triggerEndStep(PaymentOutboxMessage message, BookingPaidEvent event, SagaStatus sagaStatus) {

        log.info("Triggering booking end step for booking: {}",
                message.getBookingId());
        RoomOutboxMessage roomOutboxMessage =
                getUpdatedRoomOutboxMessage(message.getSagaId().toString(),
                        event.getBooking().getStatus(),
                        sagaStatus);
        roomOutboxService.save(roomOutboxMessage);

    }

    public RoomOutboxMessage getUpdatedRoomOutboxMessage(String sagaId,
                                                         BookingStatus bookingStatus,
                                                         SagaStatus sagaStatus) {
        Optional<RoomOutboxMessage> roomOutboxMessageResponse = roomOutboxService
                .getRoomOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(sagaId), SagaStatus.PROCESSING);
        if (roomOutboxMessageResponse.isEmpty()) {
            throw new BookingDomainException("Room outbox message cannot be found in " +
                    SagaStatus.PROCESSING.name() + " state");
        }
        RoomOutboxMessage roomOutboxMessage = roomOutboxMessageResponse.get();
        roomOutboxMessage.setProcessedAt(LocalDateTime.now());
        roomOutboxMessage.setBookingStatus(bookingStatus);
        roomOutboxMessage.setSagaStatus(sagaStatus);
        return roomOutboxMessage;
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

    // Rollback
    public PaymentOutboxMessage findPaymentOutboxMessageForRollback(PaymentMessageResponse paymentResponse) {

        SagaStatus[] validStatuses = getValidSagaStatusesForPaymentRollback(paymentResponse.getPaymentStatus());

        Optional<PaymentOutboxMessage> outboxMessageOpt =
                paymentOutboxService.getBySagaIdAndSagaStatus(
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
    public Booking rollbackToCheckOut(PaymentMessageResponse paymentResponse) {
        log.info("Executing payment rollback for booking: {}", paymentResponse.getBookingId());

        // Tìm booking entity
        Booking booking = bookingSagaHelper.findBooking(paymentResponse.getBookingId());

        // Thực hiện check-out booking
        bookingDomainService.checkOutBooking(booking);

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

        paymentOutboxService.save(updatedOutboxMessage);
    }

    /**
     * Rollback payment check out nếu payment bị cancel
     *
     * @param sagaId  ID của saga
     * @param booking Booking entity sau khi rollback
     */
    public void rollbackPaymentCancelled(String sagaId, Booking booking) {
        log.info("Rolling back room reservation for saga id: {}", sagaId);

        try {
            PaymentOutboxMessage paymentOutboxMessageForRollback =
                    findPaymentOutboxMessageForRollback(sagaId);

            SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(booking.getStatus());
            updatePaymentOutboxMessageForRollback(paymentOutboxMessageForRollback, booking.getStatus(), sagaStatus);
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
            case FAILED, EXPIRED, PENDING -> new SagaStatus[]{SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }

    /**
     * Tìm room outbox message để rollback
     *
     * @param sagaId ID của saga
     * @return BookingRoomOutboxMessage nếu tìm thấy, null nếu không tìm thấy
     */
    public PaymentOutboxMessage findPaymentOutboxMessageForRollback(String sagaId) {
        Optional<PaymentOutboxMessage> paymentOutboxMessageOptional =
                paymentOutboxService.getBySagaIdAndSagaStatus(
                        UUID.fromString(sagaId),
                        SagaStatus.COMPENSATING);

        if (paymentOutboxMessageOptional.isEmpty()) {
            throw new BookingDomainException(
                    "Room outbox message with saga id: " + sagaId +
                            " is not found in " + SagaStatus.COMPENSATING.name() + " status!");
        }

        return paymentOutboxMessageOptional.get();
    }

    /**
     * Cập nhật room outbox message với trạng thái rollback
     *
     * @param paymentOutboxMessage Room outbox message cần cập nhật
     * @param bookingStatus        Trạng thái booking mới
     * @param sagaStatus           Trạng thái saga mới
     */
    public void updatePaymentOutboxMessageForRollback(PaymentOutboxMessage paymentOutboxMessage,
                                                      BookingStatus bookingStatus,
                                                      SagaStatus sagaStatus) {
        paymentOutboxMessage.setBookingStatus(bookingStatus);
        paymentOutboxMessage.setSagaStatus(sagaStatus);
        paymentOutboxMessage.setProcessedAt(LocalDateTime.now());

        paymentOutboxService.save(paymentOutboxMessage);
    }
}
