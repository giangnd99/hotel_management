package com.poly.room.management.domain.command.room;

import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomResponseStatus;
import com.poly.room.management.domain.entity.Cost;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomCost;
import com.poly.room.management.domain.event.RoomBookedEvent;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.publisher.reponse.BookingRoomReservePublisher;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.domain.service.RoomEventService;
import com.poly.room.management.domain.message.BookingRoomRequestMessage;
import com.poly.room.management.domain.message.BookingRoomResponseMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomBookingCommand {

    private final RoomRepository roomRepository;
    private final RoomEventService roomEventService;
    private final BookingRoomReservePublisher roomBookedResponseKafkaPublisher;

    @Transactional
    public void process(BookingRoomRequestMessage bookingRoomRequestMessage) {

        log.info("Processing booking rooms request message: {}", bookingRoomRequestMessage);
        List<Room> rooms = validateRoomMessage(bookingRoomRequestMessage);

        if (BookingStatus.DEPOSITED.name().equalsIgnoreCase(bookingRoomRequestMessage.getBookingStatus())) {
            log.info("Processing DEPOSITED status for booking ID: {}", bookingRoomRequestMessage.getBookingId());

            for (Room room : rooms) {
                try {

                    room.addRoomCost(RoomCost.builder()
                            .cost(Cost.builder()
                                    .id(UUID.randomUUID().toString())
                                    .name("Deposit")
                                    .price(new Money(bookingRoomRequestMessage.getPrice()))
                                    .referenceId(bookingRoomRequestMessage.getBookingId())
                                    .build())
                            .room(room)
                            .id(UUID.randomUUID().toString())
                            .build());
                    RoomBookedEvent bookedEvent = roomEventService.bookedRoom(room);
                    // Cập nhật trạng thái phòng trong database
                    roomRepository.update(room);

                    log.info("Successfully updated room status to BOOKED for room: {}",
                            bookedEvent.getRoom().getRoomNumber());

                } catch (Exception e) {
                    log.error("Error updating room status for room: {}", room.getRoomNumber(), e);
                    throw new RuntimeException("Failed to update room status: " + room.getRoomNumber(), e);
                }
            }

            bookingRoomRequestMessage.setRooms(rooms);
            // Tạo response message để gửi về booking service
            BookingRoomResponseMessage responseMessage = createSuccessResponseMessage(bookingRoomRequestMessage);

            // Gửi response về booking service
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
        List<Room> rooms =
                bookingRoomRequestMessage.getRooms()
                .stream()
                .map(
                room -> roomRepository.findById(
                        room.getId().getValue())
                        .orElseThrow(() -> new RoomDomainException
                                ("Not found room with id " + room.getId().getValue().toString())))
                .toList();

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
                .reason("All rooms have been successfully booked and updated")
                .build();
    }
}
