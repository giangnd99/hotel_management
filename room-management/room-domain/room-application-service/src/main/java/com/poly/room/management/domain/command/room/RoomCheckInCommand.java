package com.poly.room.management.domain.command.room;

import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.RoomResponseStatus;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.event.RoomCheckedInEvent;
import com.poly.room.management.domain.message.BookingRoomRequestMessage;
import com.poly.room.management.domain.message.BookingRoomResponseMessage;
import com.poly.room.management.domain.port.out.publisher.reponse.BookingRoomReservePublisher;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.domain.service.RoomEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoomCheckInCommand {

    private final RoomRepository roomRepository;
    private final RoomEventService roomEventService;
    private final BookingRoomReservePublisher roomBookedResponseKafkaPublisher;

    public void process(BookingRoomRequestMessage bookingRoomRequestMessage) {

        log.info("Processing booking rooms request message: {}", bookingRoomRequestMessage);
        List<Room> rooms = validateRoomMessage(bookingRoomRequestMessage);

        if (BookingStatus.CHECKED_IN.name().equalsIgnoreCase(bookingRoomRequestMessage.getBookingStatus())) {
            log.info("Processing Check in status for booking ID: {}", bookingRoomRequestMessage.getBookingId());

            for (Room room : rooms) {
                try {
                    Room foundedRoom = roomRepository.findById(
                            room.getId().getValue()).orElseThrow(() -> new RuntimeException("Room not found"));
                    RoomCheckedInEvent bookedEvent = roomEventService.checkedInRoom(foundedRoom);

                    roomRepository.update(bookedEvent.getRoom());

                    log.info("Successfully updated room status to BOOKED for room: {}",
                            bookedEvent.getRoom().getRoomNumber());

                } catch (Exception e) {
                    log.error("Error updating room status for room: {}", room.getRoomNumber(), e);
                    throw new RuntimeException("Failed to update room status: " + room.getRoomNumber(), e);
                }
            }
            bookingRoomRequestMessage.setBookingStatus(BookingStatus.CHECKED_IN.name());
            bookingRoomRequestMessage.setRooms(rooms);
            bookingRoomRequestMessage.setProcessedAt(Instant.now());
            BookingRoomResponseMessage responseMessage = createSuccessResponseMessage(bookingRoomRequestMessage);


            roomBookedResponseKafkaPublisher.publish(responseMessage);

            log.info("Successfully processed all rooms for booking ID: {} and sent response",
                    bookingRoomRequestMessage.getBookingId());

        } else {
            log.warn("Unsupported booking status: {} for booking ID: {}",
                    bookingRoomRequestMessage.getBookingStatus(),
                    bookingRoomRequestMessage.getBookingId());
        }
    }

    private List<Room> validateRoomMessage(
            BookingRoomRequestMessage bookingRoomRequestMessage) {
        List<Room> rooms = bookingRoomRequestMessage.getRooms();
        if (rooms.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }
        return rooms;
    }

    private BookingRoomResponseMessage createSuccessResponseMessage(
            BookingRoomRequestMessage bookingRoomRequestMessage) {
        return BookingRoomResponseMessage.builder()
                .bookingId(bookingRoomRequestMessage.getBookingId())
                .type(bookingRoomRequestMessage.getType())
                .rooms(bookingRoomRequestMessage.getRooms())
                .totalPrice(bookingRoomRequestMessage.getPrice())
                .reservationStatus(RoomResponseStatus.SUCCESS.name())
                .id(UUID.randomUUID().toString())
                .SagaId(bookingRoomRequestMessage.getSagaId())
                .reason("All rooms have been successfully Checked In and updated")
                .build();
    }
}
