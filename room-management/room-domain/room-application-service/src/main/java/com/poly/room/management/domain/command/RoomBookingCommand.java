package com.poly.room.management.domain.command;

import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.RoomResponseStatus;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.event.RoomBookedEvent;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.domain.service.RoomEventService;
import com.poly.room.management.kafka.message.BookingRoomRequestMessage;
import com.poly.room.management.kafka.message.BookingRoomResponseMessage;
import com.poly.room.management.kafka.publisher.RoomBookedResponseKafkaPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomBookingCommand {

    private final RoomRepository roomRepository;
    private final RoomEventService roomEventService;
    private final RoomBookedResponseKafkaPublisher roomBookedResponseKafkaPublisher;

    public void process(BookingRoomRequestMessage bookingRoomRequestMessage) {

        RoomBookingCommand.log.info("Processing booking rooms request message: {}", bookingRoomRequestMessage);
        List<Room> rooms = validateRoomMessage(bookingRoomRequestMessage);

        if (BookingStatus.DEPOSITED.name().equalsIgnoreCase(bookingRoomRequestMessage.getBookingStatus())) {
            for (Room room : rooms) {
                RoomBookedEvent bookedEvent = roomEventService.bookedRoom(room);
                roomRepository.update(bookedEvent.getRoom());
                RoomBookingCommand.log.info("Room booked event sent successfully: {}", bookedEvent.getRoom().getRoomNumber());
            }
            BookingRoomResponseMessage responseMessage =
                    validateBookingRoomRequestMessage(bookingRoomRequestMessage);
            roomBookedResponseKafkaPublisher.publish(responseMessage);
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

    private BookingRoomResponseMessage validateBookingRoomRequestMessage(
            BookingRoomRequestMessage bookingRoomRequestMessage) {
        return BookingRoomResponseMessage.builder()
                .bookingId(bookingRoomRequestMessage.getBookingId())
                .type(bookingRoomRequestMessage.getType())
                .rooms(bookingRoomRequestMessage.getRooms())
                .totalPrice(bookingRoomRequestMessage.getPrice())
                .reservationStatus(RoomResponseStatus.SUCCESS.name())
                .id(UUID.randomUUID().toString())
                .build();
    }
}
