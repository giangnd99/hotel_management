package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.dto.RoomDto;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.event.BookingDepositedEvent;
import com.poly.booking.management.domain.event.BookingEvent;
import com.poly.booking.management.domain.outbox.model.room.BookingReservedEventPayload;
import com.poly.booking.management.domain.outbox.model.room.BookingRoomEventPayload;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.ReservationStatus;
import com.poly.domain.valueobject.RoomId;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public BookingReservedEventPayload bookingDepositedEventToBookingReservedEventPayload(BookingDepositedEvent domainEvent) {
        return BookingReservedEventPayload.builder()
                .bookingId(domainEvent.getBooking().getId().getValue().toString())
                .customerId(domainEvent.getBooking().getCustomerId().getValue().toString())
                .bookingRoomId(domainEvent.getBooking().getId().getValue().toString())
                .price(domainEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(domainEvent.getCreatedAt().getValue())
                .roomBookingStatus(ReservationStatus.PENDING.toString())
                .rooms(domainEvent.getBooking().getRooms().stream()
                        .map(this::roomToBookingRoomEventPayload)
                        .collect(Collectors.toList()))
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
    private BookingRoomEventPayload roomToBookingRoomEventPayload(Room room) {
        return new BookingRoomEventPayload(
                room.getId().getValue().toString(),
                room.getRoomNumber(),
                room.getBasePrice().getAmount(),
                LocalDateTime.now()
        );
    }
}
