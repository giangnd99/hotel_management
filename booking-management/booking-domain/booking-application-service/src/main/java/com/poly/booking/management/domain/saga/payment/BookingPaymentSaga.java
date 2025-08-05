package com.poly.booking.management.domain.saga.payment;

import com.poly.booking.management.domain.dto.message.PaymentMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.outbox.model.room.BookingRoomOutboxMessage;
import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentOutboxMessage;
import com.poly.booking.management.domain.outbox.scheduler.room.RoomOutboxHelper;
import com.poly.booking.management.domain.outbox.scheduler.notification.NotificationOutboxHelper;
import com.poly.booking.management.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.EBookingStatus;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * BookingPaymentSaga - Saga Step Implementation for Payment Processing
 * 
 * CHỨC NĂNG CHÍNH:
 * - Xử lý thanh toán trong quy trình Saga của hệ thống booking
 * - Quản lý trạng thái thanh toán và cập nhật outbox messages
 * - Thực hiện rollback khi thanh toán thất bại
 * 
 * MỤC ĐÍCH:
 * - Đảm bảo tính nhất quán dữ liệu trong quy trình thanh toán
 * - Xử lý bất đồng bộ thông qua Outbox Pattern
 * - Cung cấp khả năng rollback khi có lỗi thanh toán
 * 
 * ÁP DỤNG PATTERNS:
 * - Saga Pattern: Quản lý distributed transaction cho payment flow
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Domain Events: Tách biệt business logic payment
 * 
 * FLOW XỬ LÝ:
 * 1. Nhận payment response từ external payment service
 * 2. Validate outbox message để tránh duplicate processing
 * 3. Thực hiện business logic thanh toán
 * 4. Cập nhật saga status và lưu outbox messages
 * 5. Trigger next step (room reservation) nếu thanh toán thành công
 * 
 * ROLLBACK FLOW:
 * 1. Nhận payment failure/cancellation từ external service
 * 2. Tìm outbox message với trạng thái phù hợp
 * 3. Thực hiện cancel booking
 * 4. Cập nhật trạng thái đã rollback
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BookingPaymentSaga implements SagaStep<PaymentMessageResponse> {

    // ==================== DEPENDENCIES ====================
    
    // Business logic services
    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;
    private final BookingSagaHelper bookingSagaHelper;
    private final BookingDataMapper bookingDataMapper;
    
    // Outbox pattern helpers for reliable messaging
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final RoomOutboxHelper roomOutboxHelper;
    private final NotificationOutboxHelper notificationOutboxHelper;

    // ==================== SAGA STEP IMPLEMENTATION ====================

    /**
     * PROCESS METHOD - Xử lý chính của Payment Saga Step
     * 
     * LOGIC FLOW:
     * 1. Validate outbox message để tránh duplicate processing
     * 2. Thực hiện business logic thanh toán
     * 3. Cập nhật saga status và lưu outbox messages
     * 4. Trigger next step (room reservation) nếu thành công
     * 
     * OUTBOX PATTERN:
     * - Sử dụng PaymentOutboxHelper để quản lý payment outbox messages
     * - Đảm bảo payment message được xử lý một cách reliable
     * - Tránh duplicate processing thông qua saga status check
     * 
     * SAGA PATTERN:
     * - Chuyển đổi booking status sang saga status
     * - Cập nhật payment outbox message với trạng thái mới
     * - Trigger room reservation step nếu payment thành công
     */
    @Override
    @Transactional
    public void process(PaymentMessageResponse paymentResponse) {
        log.info("Processing payment for saga id: {}", paymentResponse.getSagaId());
        
        // Step 1: Validate outbox message to prevent duplicate processing
        BookingPaymentOutboxMessage outboxMessage = validateAndGetPaymentOutboxMessage(paymentResponse);
        if (outboxMessage == null) {
            return; // Already processed
        }
        
        // Step 2: Execute business logic - complete payment
        BookingPaidEvent domainEvent = executePaymentCompletion(paymentResponse);
        
        // Step 3: Update saga status and save payment outbox message
        updatePaymentOutboxMessage(outboxMessage, domainEvent);
        
        // Step 4: Trigger next step - room reservation
        triggerRoomReservationStep(domainEvent, paymentResponse.getSagaId());
        
        log.info("Payment processing completed successfully for booking: {}", 
                domainEvent.getBooking().getId().getValue());
    }

    /**
     * ROLLBACK METHOD - Xử lý rollback khi thanh toán thất bại
     * 
     * LOGIC FLOW:
     * 1. Tìm payment outbox message với trạng thái phù hợp
     * 2. Thực hiện cancel booking
     * 3. Cập nhật trạng thái đã rollback
     * 4. Rollback room reservation nếu cần
     * 
     * SAGA COMPENSATION:
     * - Thực hiện compensation action khi payment thất bại
     * - Đảm bảo tính nhất quán dữ liệu
     * - Rollback các bước đã thực hiện trước đó
     */
    @Override
    @Transactional
    public void rollback(PaymentMessageResponse paymentResponse) {
        log.info("Rolling back payment for saga id: {}", paymentResponse.getSagaId());
        
        // Step 1: Find payment outbox message for rollback
        BookingPaymentOutboxMessage outboxMessage = findPaymentOutboxMessageForRollback(paymentResponse);
        if (outboxMessage == null) {
            return; // Already rolled back
        }
        
        // Step 2: Execute rollback business logic
        Booking booking = executePaymentRollback(paymentResponse);
        
        // Step 3: Update payment outbox message with rollback status
        updatePaymentOutboxMessageForRollback(outboxMessage, booking);
        
        // Step 4: Rollback room reservation if payment was cancelled
        if (paymentResponse.getPaymentStatus() == PaymentStatus.CANCELLED) {
            rollbackRoomReservation(paymentResponse.getSagaId(), booking);
        }
        
        log.info("Payment rollback completed successfully for booking: {}", 
                booking.getId().getValue());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Validate và lấy payment outbox message để tránh duplicate processing
     * 
     * @param paymentResponse PaymentMessageResponse từ external payment service
     * @return BookingPaymentOutboxMessage nếu hợp lệ, null nếu đã processed
     */
    private BookingPaymentOutboxMessage validateAndGetPaymentOutboxMessage(PaymentMessageResponse paymentResponse) {
        Optional<BookingPaymentOutboxMessage> outboxMessageOpt = 
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
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
    private BookingPaidEvent executePaymentCompletion(PaymentMessageResponse paymentResponse) {
        log.info("Executing payment completion for booking: {}", paymentResponse.getBookingId());
        
        // Thực hiện business logic thanh toán thông qua saga helper
        return completePaymentForBooking(paymentResponse);
    }

    /**
     * Cập nhật payment outbox message với trạng thái mới
     * 
     * @param outboxMessage Payment outbox message cần cập nhật
     * @param domainEvent Domain event chứa trạng thái mới
     */
    private void updatePaymentOutboxMessage(BookingPaymentOutboxMessage outboxMessage, 
                                          BookingPaidEvent domainEvent) {
        // Chuyển đổi booking status sang saga status
        SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(
                domainEvent.getBooking().getStatus());
        
        // Tạo payment outbox message mới với trạng thái đã cập nhật
        BookingPaymentOutboxMessage updatedOutboxMessage = 
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
     * @param sagaId ID của saga
     */
    private void triggerRoomReservationStep(BookingPaidEvent domainEvent, String sagaId) {
        log.info("Triggering room reservation step for booking: {}", 
                domainEvent.getBooking().getId().getValue());
        
        // Tạo room outbox message để trigger room reservation step
        roomOutboxHelper.saveRoomOutboxMessage(
                bookingDataMapper.bookingEventToRoomBookingEventPayload(domainEvent),
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
    private BookingPaymentOutboxMessage findPaymentOutboxMessageForRollback(PaymentMessageResponse paymentResponse) {
        SagaStatus[] validStatuses = getValidSagaStatusesForPaymentRollback(paymentResponse.getPaymentStatus());
        
        Optional<BookingPaymentOutboxMessage> outboxMessageOpt = 
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
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
    private Booking executePaymentRollback(PaymentMessageResponse paymentResponse) {
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
     * @param booking Booking entity sau khi rollback
     */
    private void updatePaymentOutboxMessageForRollback(BookingPaymentOutboxMessage outboxMessage, 
                                                      Booking booking) {
        SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(booking.getStatus());
        
        BookingPaymentOutboxMessage updatedOutboxMessage = 
                createUpdatedPaymentOutboxMessage(outboxMessage, booking.getStatus(), sagaStatus);
        
        paymentOutboxHelper.save(updatedOutboxMessage);
    }

    /**
     * Rollback room reservation nếu payment bị cancel
     * 
     * @param sagaId ID của saga
     * @param booking Booking entity sau khi rollback
     */
    private void rollbackRoomReservation(String sagaId, Booking booking) {
        log.info("Rolling back room reservation for saga id: {}", sagaId);
        
        try {
            BookingRoomOutboxMessage roomOutboxMessage = 
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
     * 
     * LOGIC:
     * - COMPLETED: Chỉ rollback khi đang STARTED (payment đã hoàn tất nhưng cần rollback)
     * - CANCELLED: Rollback từ PROCESSING (payment đang xử lý bị cancel)
     * - FAILED: Rollback từ STARTED hoặc PROCESSING (payment thất bại ở bất kỳ giai đoạn nào)
     * 
     * @param paymentStatus Trạng thái thanh toán từ external payment service
     * @return Array các SagaStatus hợp lệ cho rollback
     */
    private SagaStatus[] getValidSagaStatusesForPaymentRollback(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case COMPLETED -> new SagaStatus[]{SagaStatus.STARTED};
            case CANCELLED -> new SagaStatus[]{SagaStatus.PROCESSING};
            case FAILED -> new SagaStatus[]{SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }

    /**
     * Tạo payment outbox message đã cập nhật
     * 
     * @param outboxMessage Payment outbox message gốc
     * @param bookingStatus Trạng thái booking mới
     * @param sagaStatus Trạng thái saga mới
     * @return BookingPaymentOutboxMessage đã cập nhật
     */
    private BookingPaymentOutboxMessage createUpdatedPaymentOutboxMessage(
            BookingPaymentOutboxMessage outboxMessage, 
            EBookingStatus bookingStatus, 
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
    private BookingRoomOutboxMessage findRoomOutboxMessageForRollback(String sagaId) {
        Optional<BookingRoomOutboxMessage> roomOutboxMessageOpt = 
                roomOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
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
     * @param bookingStatus Trạng thái booking mới
     * @param sagaStatus Trạng thái saga mới
     */
    private void updateRoomOutboxMessageForRollback(BookingRoomOutboxMessage roomOutboxMessage,
                                                   EBookingStatus bookingStatus,
                                                   SagaStatus sagaStatus) {
        roomOutboxMessage.setBookingStatus(bookingStatus);
        roomOutboxMessage.setSagaStatus(sagaStatus);
        roomOutboxMessage.setProcessedAt(LocalDateTime.now());
        
        roomOutboxHelper.save(roomOutboxMessage);
    }

    /**
     * Thực hiện business logic hoàn tất thanh toán
     * 
     * @param data PaymentMessageResponse chứa thông tin thanh toán
     * @return BookingPaidEvent domain event
     */
    private BookingPaidEvent completePaymentForBooking(PaymentMessageResponse data) {
        log.info("Completing payment for booking with id: {}", data.getBookingId());
        Booking booking = bookingSagaHelper.findBooking(data.getBookingId());
        BookingPaidEvent domainEvent = bookingDomainService.payBooking(booking);
        bookingRepository.save(booking);
        return domainEvent;
    }
}

