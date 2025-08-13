package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.dto.RoomDto;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.BookingRoom;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.event.BookingDepositedEvent;
import com.poly.booking.management.domain.event.CheckInEvent;
import com.poly.booking.management.domain.outbox.payload.ReservedEventPayload;
import com.poly.booking.management.domain.outbox.payload.RoomEventPayload;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomResponseStatus;
import com.poly.domain.valueobject.RoomId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class RoomDataMapper {


    /**
     * Chuyển đổi danh sách RoomDto thành danh sách Room entity
     * <p>
     * Mục đích: Chuyển đổi dữ liệu từ request DTO sang domain entity
     * Sử dụng: Khi tạo booking từ CreateBookingCommand
     *
     * @param roomsDto Danh sách RoomDto từ request
     * @return Danh sách Room entity
     */
    public List<Room> roomsDtoToRooms(List<RoomDto> roomsDto) {
        return roomsDto.stream().map(
                roomDto ->
                        new Room(new RoomId(UUID.fromString(roomDto.getRoomId())),
                                roomDto.getRoomNumber(),
                                Money.from(roomDto.getBasePrice()),
                                roomDto.getStatus())
        ).toList();
    }

    /**
     * Chuyển đổi BookingPaidEvent thành BookingReservedEventPayload (alias method)
     * <p>
     * Mục đích: Tạo payload cho Kafka message để thông báo room service về việc đặt phòng đã thanh toán
     * Sử dụng: Gửi message đến room service để cập nhật trạng thái phòng
     *
     * @param domainEvent BookingPaidEvent từ domain
     * @return BookingReservedEventPayload cho Kafka message
     */
    public ReservedEventPayload bookingDepositedEventToBookingReservedEventPayload(BookingDepositedEvent domainEvent) {
        return ReservedEventPayload.builder()
                .bookingId(domainEvent.getBooking().getId().getValue().toString())
                .customerId(domainEvent.getBooking().getCustomerId().getValue().toString())
                .price(domainEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(domainEvent.getCreatedAt().getValue())
                .roomBookingStatus(RoomResponseStatus.PENDING.toString())
                .rooms(domainEvent.getBooking().getBookingRooms().stream()
                        .map(bookingRoom ->
                                roomToBookingRoomEventPayload(bookingRoom.getRoom()))
                        .toList())
                .build();
    }

    /**
     * Chuyển đổi Room entity thành BookingRoomEventPayload
     * <p>
     * Mục đích: Tạo payload cho room information trong booking event
     * Sử dụng: Trong booking event payload để gửi thông tin phòng
     *
     * @param room Room entity
     * @return BookingRoomEventPayload
     */
    private RoomEventPayload roomToBookingRoomEventPayload(Room room) {
        return new RoomEventPayload(
                room.getId().getValue().toString(),
                room.getRoomNumber(),
                room.getBasePrice().getAmount(),
                LocalDateTime.now()
        );
    }

    public ReservedEventPayload bookingCheckInEventToRoomBookedEventPayload(CheckInEvent domainEvent) {
        return ReservedEventPayload.builder()
                .bookingId(domainEvent.getBooking().getId().getValue().toString())
                .roomBookingStatus(RoomResponseStatus.SUCCESS.name())
                .rooms(domainEvent.getBooking()
                        .getBookingRooms()
                        .stream()
                        .map(bookingRoom ->
                                roomToBookingRoomEventPayload(bookingRoom.getRoom())
                        ).toList())
                .price(domainEvent.getBooking().getTotalPrice().getAmount())
                .customerId(domainEvent.getBooking().getCustomerId().getValue().toString())
                .createdAt(domainEvent.getCreatedAt().getValue())
                .build();
    }

    public List<BookingRoom> mapRoomWithBooking(Booking booking, List<Room> roomsRequest) {
        return roomsRequest.stream().map(room -> {
            return BookingRoom.builder()
                    .booking(booking)
                    .room(room)
                    .build();
        }).toList();
    }
}
