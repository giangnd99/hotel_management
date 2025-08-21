package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.event.BookingDepositedEvent;
import com.poly.booking.management.domain.event.CheckInEvent;
import com.poly.booking.management.domain.outbox.payload.ReservedEventPayload;
import com.poly.booking.management.domain.outbox.payload.RoomEventPayload;
import com.poly.domain.valueobject.RoomResponseStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RoomDataMapper {

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
                .customerId(domainEvent.getBooking().getCustomer().getId().getValue().toString())
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
                .customerId(domainEvent.getBooking().getCustomer().getId().getValue().toString())
                .createdAt(domainEvent.getCreatedAt().getValue())
                .build();
    }
}
