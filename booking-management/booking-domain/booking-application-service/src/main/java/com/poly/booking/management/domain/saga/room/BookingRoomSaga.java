package com.poly.booking.management.domain.saga.room;

import com.poly.booking.management.domain.dto.message.room.RoomMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingDepositEvent;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.outbox.model.room.BookingRoomOutboxMessage;
import com.poly.booking.management.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.poly.booking.management.domain.outbox.scheduler.room.RoomOutboxHelper;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.domain.valueobject.ReservationStatus;
import com.poly.saga.SagaStatus;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingRoomSaga implements SagaStep<RoomMessageResponse> {

    private final BookingDataMapper bookingDataMapper;
    private final RoomOutboxHelper roomOutboxHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final BookingSagaHelper bookingSagaHelper;
    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;

    @Override
    public void process(RoomMessageResponse data) {
        Optional<BookingRoomOutboxMessage> bookingApprovalOutboxMessage =
                roomOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.PROCESSING);
        if (bookingApprovalOutboxMessage.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed!", data.getSagaId());
            return;
        }
        BookingRoomOutboxMessage bookingRoomOutboxMessageResponse = bookingApprovalOutboxMessage.get();

        BookingDepositEvent domainEvent = reservedRoom(bookingRoomOutboxMessageResponse);

        SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(domainEvent.getBooking().getStatus());

        BookingRoomOutboxMessage roomUpdatedOutboxMessage =
                roomOutboxHelper.getUpdatedRoomOutBoxMessage(
                        bookingRoomOutboxMessageResponse,
                        domainEvent.getBooking().getStatus(),
                        sagaStatus);
        roomOutboxHelper.save(roomUpdatedOutboxMessage);
        log.info("Booking with id: {} has been confirmed successfully!", domainEvent.getBooking().getId().getValue());

    }

    private BookingDepositEvent reservedRoom(BookingRoomOutboxMessage bookingRoomOutboxMessageResponse) {
        log.info("Reserving room with id: {}", bookingRoomOutboxMessageResponse.getPayload());
        Booking booking = bookingSagaHelper.findBooking(bookingRoomOutboxMessageResponse.getBookingId());
        BookingDepositEvent domainEvent = bookingDomainService.confirmDepositBooking(booking);
        bookingRepository.save(booking);
        return domainEvent;
    }

    @Override
    public void rollback(RoomMessageResponse data) {
        Optional<BookingRoomOutboxMessage> response =
                roomOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        getCurrentSagaStatus(data.getReservationStatus()));
        if (response.isEmpty()) {
            log.info("An outbox message with saga id: {} is already roll backed!", data.getSagaId());
            return;
        }
        BookingRoomOutboxMessage bookingRoomOutboxMessage = response.get();
        Booking booking = bookingSagaHelper.findBooking(bookingRoomOutboxMessage.getBookingId());
        bookingDomainService.cancelBooking(booking);
        bookingRepository.save(booking);
        log.info("Booking with id: {} has been roll backed successfully!", booking.getId().getValue());
    }


    private SagaStatus[] getCurrentSagaStatus(ReservationStatus reservationStatus) {
        return switch (reservationStatus) {
            case SUCCESS -> new SagaStatus[]{SagaStatus.PROCESSING};
            case FAILED, CANCELLED -> new SagaStatus[]{SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }
}
