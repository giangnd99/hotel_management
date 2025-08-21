package com.poly.booking.management.domain.saga.room;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingConfirmedEvent;
import com.poly.booking.management.domain.mapper.NotificationDataMapper;
import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.NotificationOutboxService;
import com.poly.booking.management.domain.outbox.service.impl.RoomOutboxServiceImpl;
import com.poly.booking.management.domain.port.out.message.publisher.NotificationRequestMessagePublisher;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.saga.notification.NotificationSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.domain.valueobject.RoomResponseStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.poly.saga.booking.SagaConstant.BOOKING_SAGA_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomSagaHelper {

    // Dependencies for data mapping and business logic
    private final NotificationDataMapper notificationDataMapper;
    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;

    // Saga orchestration helpers
    private final BookingSagaHelper bookingSagaHelper;

    // Outbox pattern helpers for reliable messaging
    private final RoomOutboxServiceImpl roomOutboxServiceImpl;
    private final NotificationOutboxService notificationOutboxService;
    private final NotificationSagaHelper notificationSagaHelper;
    private final NotificationRequestMessagePublisher notificationRequestMessagePublisher;
    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Validate và lấy outbox message để tránh duplicate processing
     *
     * @param data RoomMessageResponse từ external service
     * @return BookingRoomOutboxMessage nếu hợp lệ, null nếu đã processed
     */
    public List<RoomOutboxMessage> validateAndGetOutboxMessage(RoomMessageResponse data) {
        Optional<List<RoomOutboxMessage>> outboxMessageOpt =
                roomOutboxServiceImpl.getRoomOutboxMessagesBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.PROCESSING);

        if (outboxMessageOpt.isEmpty()) {
            log.info("Outbox message with saga id: {} already processed!", data.getSagaId());
            return null;
        }

        return outboxMessageOpt.get();
    }

    /**
     * Thực hiện business logic đặt phòng
     *
     * @param outboxMessage Outbox message chứa thông tin booking
     * @return BookingDepositEvent domain event
     */
    @Transactional
    public BookingConfirmedEvent executeRoomReservation(RoomOutboxMessage outboxMessage) {
        log.info("Executing room reservation for booking: {}", outboxMessage.getBookingId());

        // Tìm booking entity
        Booking booking = bookingSagaHelper.findBooking(outboxMessage.getBookingId());

        // Thực hiện business logic confirm deposit
        BookingConfirmedEvent domainEvent = bookingDomainService.confirmBooking(booking);

        // Lưu trạng thái mới
        bookingRepository.save(booking);

        return domainEvent;
    }


    /**
     * Trigger bước tiếp theo - send qr code notification
     *
     * @param domainEvent Domain event chứa thông tin booking
     * @param data        Room message từ message kafka
     */
    public void triggerSendQrCodeStep(BookingConfirmedEvent domainEvent, RoomMessageResponse data) {
        log.info("Triggering room reservation step for booking: {}",
                domainEvent.getBooking().getId().getValue());

        String payload = notificationOutboxService.createPayload(notificationDataMapper.bookingRoomReservedEventToBookingNotifiEventPayload(domainEvent));

        NotifiOutboxMessage notifiOutboxMessage = NotifiOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(UUID.fromString(data.getSagaId()))
                .type(BOOKING_SAGA_NAME)
                .sagaStatus(SagaStatus.PROCESSING)
                .outboxStatus(OutboxStatus.STARTED)
                .payload(payload)
                .bookingStatus(domainEvent.getBooking().getStatus())
                .createdAt(LocalDateTime.now())
                .bookingId(domainEvent.getBooking().getId().getValue())
                .build();


        notificationRequestMessagePublisher.sendNotifi(notifiOutboxMessage,updateOutboxStatus(notifiOutboxMessage,OutboxStatus.COMPLETED) );
    }

    private BiConsumer<NotifiOutboxMessage, OutboxStatus> updateOutboxStatus(NotifiOutboxMessage notifiOutboxMessage, OutboxStatus outboxStatus) {
        notifiOutboxMessage.setOutboxStatus(outboxStatus);
        log.info("OrderApprovalOutboxMessage is updated with outbox status: {}", outboxStatus.name());
        return null;
    }

    /**
     * Cập nhật saga status và lưu outbox message
     *
     * @param outboxMessage Outbox message cần cập nhật
     * @param domainEvent   Domain event chứa trạng thái mới
     */
    public void updateSagaStatusAndSaveOutbox(RoomOutboxMessage outboxMessage,
                                              BookingConfirmedEvent domainEvent) {
        // Chuyển đổi booking status sang saga status
        SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(
                domainEvent.getBooking().getStatus());

        // Tạo outbox message mới với trạng thái đã cập nhật
        log.info("Updating room reservation outbox message with bookingId: {}", domainEvent.getBooking().getId().getValue());
        outboxMessage.setBookingId(domainEvent.getBooking().getId().getValue());
        outboxMessage.setId(outboxMessage.getId());
        outboxMessage.setCreatedAt(LocalDateTime.now());
        outboxMessage.setType(RoomOutboxMessage.class.getSimpleName());

        RoomOutboxMessage updatedOutboxMessage =
                roomOutboxServiceImpl.getUpdatedRoomOutBoxMessage(
                        outboxMessage,
                        domainEvent.getBooking().getStatus(),
                        sagaStatus);
        log.info("Updated room reservation outbox message: {}", updatedOutboxMessage);
    }

    /**
     * Tìm outbox message phù hợp để rollback
     *
     * @param data RoomMessageResponse chứa thông tin rollback
     * @return BookingRoomOutboxMessage nếu tìm thấy, null nếu đã rollback
     */
    public RoomOutboxMessage findOutboxMessageForRollback(RoomMessageResponse data) {
        SagaStatus[] validStatuses = getValidSagaStatusesForRollback(data.getRoomBookingStatus());

        Optional<RoomOutboxMessage> outboxMessageOpt =
                roomOutboxServiceImpl.getRoomOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        validStatuses);

        if (outboxMessageOpt.isEmpty()) {
            log.info("Outbox message with saga id: {} already rolled back!", data.getSagaId());
            return null;
        }

        return outboxMessageOpt.get();
    }

    /**
     * Thực hiện rollback business logic
     *
     * @param outboxMessage Outbox message cần rollback
     */
    public void executeRollback(RoomOutboxMessage outboxMessage) {
        // Tìm booking entity
        Booking booking = bookingSagaHelper.findBooking(outboxMessage.getBookingId());

        // Thực hiện cancel booking
        bookingDomainService.cancelBooking(booking);

        // Lưu trạng thái đã cancel
        bookingRepository.save(booking);
    }

    /**
     * Xác định các saga status hợp lệ cho rollback dựa trên reservation status
     * <p>
     * LOGIC:
     * - SUCCESS: Chỉ rollback khi đang PROCESSING
     * - FAILED/CANCELLED: Rollback từ STARTED hoặc PROCESSING
     *
     * @param roomResponseStatus Trạng thái reservation từ external service
     * @return Array các SagaStatus hợp lệ cho rollback
     */
    public SagaStatus[] getValidSagaStatusesForRollback(RoomResponseStatus roomResponseStatus) {
        return switch (roomResponseStatus) {
            case SUCCESS -> new SagaStatus[]{SagaStatus.PROCESSING};
            case PENDING, FAILED, CANCELLED -> new SagaStatus[]{SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }
}
