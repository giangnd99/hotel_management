package com.poly.booking.management.domain.mapper;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.event.BookingDepositedEvent;
import com.poly.booking.management.domain.event.CheckInEvent;
import com.poly.booking.management.domain.outbox.payload.ReservedEventPayload;
import com.poly.booking.management.domain.outbox.payload.RoomEventPayload;
import com.poly.domain.valueobject.RoomResponseStatus;
import com.poly.domain.valueobject.RoomStatus;
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
    public ReservedEventPayload bookingDepositedEventToBookingReservedEventPayload(Booking domainEvent) {
        return ReservedEventPayload.builder()
                .bookingId(domainEvent.getId().getValue().toString())
                .customerId(domainEvent.getCustomer().getId().getValue().toString())
                .price(domainEvent.getTotalPrice().getAmount())
                .createdAt(LocalDateTime.now())
                .roomBookingStatus(RoomResponseStatus.PENDING.toString())
                .rooms(domainEvent.getBookingRooms().stream()
                        .map(bookingRoom ->
                                roomToRoomBookedEventPayload(bookingRoom.getRoom()))
                        .toList())
                .build();
    }

    /// Sai ở đây
    /**
     * Chuyển đổi Room entity thành BookingRoomEventPayload
     * <p>
     * Mục đích: Tạo payload cho room information trong booking event
     * Sử dụng: Trong booking event payload để gửi thông tin phòng
     *
     * @param room Room entity
     * @return BookingRoomEventPayload
     */
    private RoomEventPayload roomToRoomBookedEventPayload(Room room) {
        return RoomEventPayload.builder()
                .roomId(room.getId().getValue().toString())
                .roomStatus(RoomStatus.BOOKED.name())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private RoomEventPayload roomToRoomCheckOutEventPayload(Room room) {
        return RoomEventPayload.builder()
                .roomId(room.getId().getValue().toString())
                .createdAt(LocalDateTime.now())
                .roomStatus(RoomStatus.CHECKED_OUT.name())
                .build();
    }

    public ReservedEventPayload bookingCheckInEventToRoomBookedEventPayload(Booking domainEvent) {
        return ReservedEventPayload.builder()
                .bookingId(domainEvent.getId().getValue().toString())
                .roomBookingStatus(RoomResponseStatus.SUCCESS.name())
                .rooms(domainEvent.getBookingRooms()
                        .stream()
                        .map(bookingRoom ->
                                roomToRoomCheckOutEventPayload(bookingRoom.getRoom())
                        ).toList())
                .price(domainEvent.getTotalPrice().getAmount())
                .customerId(domainEvent.getCustomer().getId().getValue().toString())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
