package com.poly.booking.management.domain.saga.notification;

import com.poly.booking.management.domain.dto.message.NotificationMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.CheckInEvent;
import com.poly.booking.management.domain.event.CheckOutEvent;
import com.poly.booking.management.domain.mapper.PaymentDataMapper;
import com.poly.booking.management.domain.outbox.payload.NotifiEventPayload;
import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.booking.management.domain.outbox.service.impl.NotificationOutboxServiceImpl;
import com.poly.booking.management.domain.outbox.service.impl.PaymentOutboxImpl;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.BookingId;
import com.poly.domain.valueobject.EBookingStatus;
import com.poly.domain.valueobject.NotificationStatus;
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
 * BookingNotifiSaga - Saga Step Implementation for QR Check-in Notification Processing
 * <p>
 * CHỨC NĂNG CHÍNH:
 * - Xử lý notification sau khi quét QR code để check-in
 * - Quản lý trạng thái check-in và cập nhật outbox messages
 * - Thực hiện rollback khi check-in thất bại
 * <p>
 * MỤC ĐÍCH:
 * - Đảm bảo tính nhất quán dữ liệu trong quy trình check-in
 * - Xử lý bất đồng bộ thông qua Outbox Pattern
 * - Cung cấp khả năng rollback khi có lỗi check-in
 * <p>
 * ÁP DỤNG PATTERNS:
 * - Saga Pattern: Quản lý distributed transaction cho check-in flow
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Domain Events: Tách biệt business logic check-in
 * <p>
 * FLOW XỬ LÝ CHECK-IN:
 * 1. Nhận notification response từ QR scanning service
 * 2. Validate outbox message để tránh duplicate processing
 * 3. Thực hiện business logic check-in (cập nhật booking status)
 * 4. Cập nhật saga status và lưu outbox messages
 * 5. Trigger next step (payment processing) nếu check-in thành công
 * <p>
 * ROLLBACK FLOW:
 * 1. Nhận check-in failure từ notification service
 * 2. Tìm outbox message với trạng thái phù hợp
 * 3. Thực hiện cancel check-in và revert booking status
 * 4. Cập nhật trạng thái đã rollback
 * <p>
 * QR CHECK-IN PROCESS:
 * - Customer quét QR code tại hotel
 * - QR service gửi notification với booking info
 * - Saga xử lý check-in và cập nhật booking status
 * - Gửi confirmation notification cho customer
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CheckInStep implements SagaStep<NotificationMessageResponse> {

    private final NotificationSagaHelper notificationSagaHelper;

    // ==================== SAGA STEP IMPLEMENTATION ====================

    /**
     * PROCESS METHOD - Xử lý chính của Notification Saga Step
     * <p>
     * LOGIC FLOW:
     * 1. Validate outbox message để tránh duplicate processing
     * 2. Thực hiện business logic check-in
     * 3. Cập nhật saga status và lưu outbox messages
     * 4. Trigger next step (payment) nếu thành công
     * <p>
     * OUTBOX PATTERN:
     * - Sử dụng NotificationOutboxHelper để quản lý notification outbox messages
     * - Đảm bảo notification message được xử lý một cách reliable
     * - Tránh duplicate processing thông qua saga status check
     * <p>
     * SAGA PATTERN:
     * - Chuyển đổi booking status sang CHECKED_IN
     * - Cập nhật notification outbox message với trạng thái mới
     * - Trigger payment step nếu check-in thành công
     *
     * @param notificationMessageResponse NotificationMessageResponse chứa thông tin QR check-in
     */
    @Override
    @Transactional
    public void process(NotificationMessageResponse notificationMessageResponse) {

        NotifiOutboxMessage notifiOutboxMessage = notificationSagaHelper.validateAndGetOutboxMessage(notificationMessageResponse);
        if (notifiOutboxMessage == null) {
            return;
        }

        // Step 2: Tìm booking và validate trạng thái
        Booking booking = notificationSagaHelper.findBookingAndValidateStatus(notificationMessageResponse.getBookingId());

        // Step 3: Thực hiện business logic check-in
        notificationSagaHelper.performCheckInBusinessLogic(booking, notificationMessageResponse);

        // Step 4: Cập nhật saga status và lưu outbox messages
//        notificationSagaHelper.updateSagaStatusAndOutboxMessages(booking, notificationMessageResponse, SagaStatus.PROCESSING);

        // Step 5: Trigger next step (payment processing) nếu check-in thành công
        if (EBookingStatus.CHECKED_IN.equals(notificationMessageResponse.getBookingStatus())) {
            notificationSagaHelper.triggerNextSagaStep(booking, notificationMessageResponse);
        }

        log.info("Notification saga step processed successfully for booking: {}", notificationMessageResponse.getBookingId());
    }


    /**
     * ROLLBACK METHOD - Xử lý rollback khi có lỗi
     * <p>
     * LOGIC FLOW:
     * 1. Tìm outbox message với trạng thái phù hợp
     * 2. Thực hiện cancel check-in và revert booking status
     * 3. Cập nhật trạng thái đã rollback
     * <p>
     * COMPENSATION ACTIONS:
     * - Revert booking status từ CHECKED_IN về CONFIRMED
     * - Cập nhật notification outbox message với trạng thái FAILED
     * - Gửi notification cho customer về check-in failure
     *
     * @param data NotificationMessageResponse chứa thông tin cần rollback
     */
    @Override
    @Transactional
    public void rollback(NotificationMessageResponse data) {
        NotifiOutboxMessage notifiOutboxMessage = notificationSagaHelper.validateAndGetOutboxMessageForRollback(data);
        if (notifiOutboxMessage == null) {
            return;
        }

        // Step 2: Thực hiện rollback business logic
        notificationSagaHelper.performRollbackBusinessLogic(data);

        // Step 3: Cập nhật outbox message với trạng thái rollback
        notificationSagaHelper.updateOutboxMessageForRollback(notifiOutboxMessage, data);

        log.info("Notification saga step rolled back successfully for booking: {}", data.getBookingId());

    }

}
