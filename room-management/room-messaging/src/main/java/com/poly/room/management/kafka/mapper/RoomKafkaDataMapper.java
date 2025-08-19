package com.poly.room.management.kafka.mapper;

import com.poly.booking.management.domain.kafka.model.BookingRoomRequestAvro;
import com.poly.booking.management.domain.kafka.model.BookingRoomResponseAvro;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;
import com.poly.domain.valueobject.RoomStatus;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.message.BookingRoomRequestMessage;
import com.poly.room.management.domain.message.BookingRoomResponseMessage;
import com.poly.room.management.domain.message.RoomCancellationRequestMessage;
import com.poly.room.management.domain.message.RoomCancellationResponseMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RoomKafkaDataMapper {

    public BookingRoomResponseAvro toBookingRoomResponseAvro(BookingRoomResponseMessage bookingRoomResponseMessage) {
        return BookingRoomResponseAvro.newBuilder()
                .setBookingId(bookingRoomResponseMessage.getBookingId())
                .setId(bookingRoomResponseMessage.getId())
                .setRooms(toRoomAvro(bookingRoomResponseMessage.getRooms()))
                .setReason(bookingRoomResponseMessage.getReason())
                .setSagaId(bookingRoomResponseMessage.getSagaId())
                .setTotalPrice(bookingRoomResponseMessage.getTotalPrice())
                .setReservationStatus(bookingRoomResponseMessage.getReservationStatus())
                .build();
    }

    private List<Room> roomsAvroToRooms(List<com.poly.booking.management.domain.kafka.model.Room> roomsAvro) {
        return roomsAvro.stream().map(roomAvro ->
                Room.Builder.builder()
                        .id(new RoomId(UUID.fromString(roomAvro.getId())))
                        .roomNumber(roomAvro.getRoomNumber())
                        .roomStatus(RoomStatus.valueOf(roomAvro.getStatus()))
                        .roomPrice(Money.from(roomAvro.getBasePrice()))
                        .build()
        ).toList();
    }

    public List<com.poly.booking.management.domain.kafka.model.Room> toRoomAvro(List<Room> rooms) {
        return rooms.stream().map(room ->
                com.poly.booking.management.domain.kafka.model.Room.newBuilder()
                        .setId(room.getId().getValue().toString())
                        .setRoomNumber(room.getRoomNumber())
                        .setBasePrice(room.getRoomPrice().getAmount().toString())
                        .setStatus(room.getRoomStatus().name())
                        .build()).toList();
    }

    public BookingRoomRequestMessage toBookingRoomRequestMessage(BookingRoomRequestAvro bookingRoomRequestAvro) {
        return BookingRoomRequestMessage.builder()
                .id(bookingRoomRequestAvro.getId().toString())
                .bookingId(bookingRoomRequestAvro.getBookingId())
                .bookingStatus(bookingRoomRequestAvro.getBookingStatus())
                .type(bookingRoomRequestAvro.getType())
                .createdAt(bookingRoomRequestAvro.getCreatedAt())
                .processedAt(bookingRoomRequestAvro.getProcessedAt())
                .price(bookingRoomRequestAvro.getPrice())
                .SagaId(bookingRoomRequestAvro.getSagaId().toString())
                .sagaStatus(bookingRoomRequestAvro.getSagaStatus())
                .build();
    }

    public RoomCancellationRequestMessage roomCancellationRequestMessage(BookingRoomRequestAvro bookingRoomRequestAvro) {
        return RoomCancellationRequestMessage.builder()
                .roomId(UUID.fromString(bookingRoomRequestAvro.getBookingId()))
                .bookingId(UUID.fromString(bookingRoomRequestAvro.getBookingId()))
                .cancellationReason(bookingRoomRequestAvro.getType())
                .build();
    }

    public RoomCancellationResponseMessage toRoomCancellationResponseMessage(BookingRoomResponseAvro bookingRoomResponseAvro) {
        return RoomCancellationResponseMessage.builder()
                .bookingId(UUID.fromString(bookingRoomResponseAvro.getBookingId()))
                .roomId(UUID.fromString(bookingRoomResponseAvro.getBookingId()))
                .cancellationStatus(bookingRoomResponseAvro.getReservationStatus())
                .message(bookingRoomResponseAvro.getReason())
                .build();
    }

    /**
     * Chuyển đổi BookingRoomRequestMessage thành BookingRoomRequestAvro
     * <p>
     * NGHIỆP VỤ:
     * - Chuyển đổi domain message thành Avro model để gửi qua Kafka
     * - Đảm bảo tính nhất quán dữ liệu giữa các service
     * <p>
     * FLOW XỬ LÝ:
     * 1. Nhận domain message
     * 2. Chuyển đổi các field tương ứng
     * 3. Tạo và trả về Avro model
     *
     * @param bookingRoomRequestMessage Domain message cần chuyển đổi
     * @return BookingRoomRequestAvro model
     */
    public BookingRoomRequestAvro toBookingRoomRequestAvro(BookingRoomRequestMessage bookingRoomRequestMessage) {
        return BookingRoomRequestAvro.newBuilder()
                .setId(UUID.fromString(bookingRoomRequestMessage.getId()))
                .setSagaId(UUID.fromString(bookingRoomRequestMessage.getSagaId()))
                .setBookingId(bookingRoomRequestMessage.getBookingId())
                .setCreatedAt(bookingRoomRequestMessage.getCreatedAt())
                .setProcessedAt(bookingRoomRequestMessage.getProcessedAt())
                .setType(bookingRoomRequestMessage.getType())
                .setSagaStatus(bookingRoomRequestMessage.getSagaStatus())
                .setBookingStatus(bookingRoomRequestMessage.getBookingStatus())
                .setPrice(bookingRoomRequestMessage.getPrice())
                .setRooms(bookingRoomRequestMessage.getRooms() != null ?
                        toRoomAvro(bookingRoomRequestMessage.getRooms()) : null)
                .build();
    }
}
