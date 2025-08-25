package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.booking.management.domain.outbox.payload.NotifiEventPayload;
import com.poly.booking.management.domain.outbox.service.NotificationOutboxService;
import com.poly.booking.management.domain.port.out.client.RoomClient;
import com.poly.booking.management.domain.port.out.message.publisher.NotificationRequestMessagePublisher;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.port.out.repository.RoomRepository;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.RoomStatus;
import com.poly.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookingCancellationDomainService {

    private static final int CANCELLATION_DEADLINE_DAYS = 1;
    private final BookingRepository bookingRepository;
    private final NotificationRequestMessagePublisher notificationRequestMessagePublisher;
    private final NotificationOutboxService notificationOutboxService;
    private final RoomClient roomClient;
    private final RoomRepository roomRepository;

    public Booking cancelBooking(UUID bookingId, String cancellationReason) {
        try {
            log.info("Processing cancellation for booking: {} with reason: {}",
                    bookingId, cancellationReason);

            Booking bookingFounded = validateBookingCanBeCancelled(bookingId);

            boolean isRefundable = determineRefundability(bookingFounded);

            if (isAfterCheckIn(bookingFounded)) {
                isRefundable = false;
            }

            if (isRefundable) {
                log.info("Booking cancellation is refundable: {}", bookingFounded.getId().getValue());
                NotifiOutboxMessage notifiOutboxMessage = createNotifiOutboxMessage(bookingFounded);
                notificationRequestMessagePublisher.sendNotifiCancel(notifiOutboxMessage
                        , createBookingCancelledEventConsumer(bookingFounded));
            }

            bookingFounded.cancelBooking();

            List<UUID> roomIds = bookingFounded.getBookingRooms()
                    .stream()
                    .map(bookingRoom -> {
                        UUID roomId = bookingRoom.getRoom().getId().getValue();
                        Room room = roomRepository.findById(roomId).orElseThrow(() -> new BookingDomainException("Room not found"));
                        log.info("Canceling room id: {}", roomId);
                        room.setStatus(RoomStatus.VACANT);
                        return room.getId().getValue();
                    })
                    .toList();

            roomClient.cancelRoom(roomIds);

            log.info("Booking cancelled successfully: {}. Refundable: {}. Reason: {}",
                    bookingFounded.getId().getValue(), isRefundable, cancellationReason);

            return bookingFounded;
        } catch (Exception e) {
            log.error("Error when cancel booking: {}", e.getMessage());
            throw new BookingDomainException("Error when cancel booking: " + e.getMessage());
        }
    }

    private NotifiOutboxMessage createNotifiOutboxMessage(Booking booking) {
        NotifiEventPayload eventPayload = NotifiEventPayload.builder()
                .id(UUID.randomUUID())
                .bookingId(booking.getId().getValue())
                .bookingStatus(BookingStatus.CANCELLED)
                .customerEmail(booking.getCustomer().getEmail())
                .checkInTime(booking.getCheckInDate().getValue())
                .createdAt(LocalDateTime.now())
                .notificationStatus("PENDING")
                .build();

        return NotifiOutboxMessage.builder()
                .bookingId(booking.getId().getValue())
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .bookingStatus(BookingStatus.CANCELLED)
                .outboxStatus(OutboxStatus.STARTED)
                .processedAt(LocalDateTime.now())
                .sagaId(UUID.randomUUID())
                .type("BookingCancelled")
                .payload(notificationOutboxService.createPayload(eventPayload))
                .build();
    }

    private BiConsumer<NotifiOutboxMessage, OutboxStatus> createBookingCancelledEventConsumer(Booking booking) {
        return (b, e) -> {
            log.info("Booking cancelled event received for booking id: {}", b.getBookingId());
        };
    }

    private Booking validateBookingCanBeCancelled(UUID bookingId) {
        if (bookingId == null) {
            throw new BookingDomainException("Booking cannot be null for cancellation");
        }

        Booking bookingFounded = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingDomainException("Not found Booking"));

        BookingStatus currentStatus = bookingFounded.getStatus();

        if (currentStatus == BookingStatus.CANCELLED) {
            throw new BookingDomainException("Booking is already cancelled: " + bookingFounded.getId().getValue());
        }

        log.debug("Booking validation passed for cancellation: {}", bookingFounded.getId().getValue());

        return bookingFounded;
    }


    private boolean determineRefundability(Booking booking) {
        DateCustom checkInDate = booking.getCheckInDate();
        if (checkInDate == null) {
            log.warn("Check-in date is null for booking: {}. Assuming refundable.", booking.getId().getValue());
            return true;
        }

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime checkInLocalDate = checkInDate.getValue();

        long daysUntilCheckIn = ChronoUnit.DAYS.between(today, checkInLocalDate);

        boolean isRefundable = daysUntilCheckIn > CANCELLATION_DEADLINE_DAYS;

        log.info("Cancellation refundability for booking {}: {} days until check-in, refundable: {}",
                booking.getId().getValue(), daysUntilCheckIn, isRefundable);

        return isRefundable;
    }

    private boolean isAfterCheckIn(Booking booking) {
        DateCustom checkInDate = booking.getCheckInDate();
        if (checkInDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate checkInLocalDate = checkInDate.getValue().toLocalDate();

        return today.isAfter(checkInLocalDate);
    }

    public long getDaysUntilCheckIn(Booking booking) {
        DateCustom checkInDate = booking.getCheckInDate();
        if (checkInDate == null) {
            return -1;
        }

        LocalDate today = LocalDate.now();
        LocalDate checkInLocalDate = checkInDate.getValue().toLocalDate();

        return ChronoUnit.DAYS.between(today, checkInLocalDate);
    }

}
