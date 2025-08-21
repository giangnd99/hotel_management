package com.poly.booking.management.messaging.mapper;

import com.poly.booking.management.domain.kafka.model.*;
import com.poly.booking.management.domain.kafka.model.PaymentStatus;
import com.poly.booking.management.domain.message.reponse.BookingPaymentPendingResponse;
import com.poly.booking.management.domain.outbox.payload.ReservedEventPayload;
import com.poly.booking.management.domain.message.reponse.CustomerCreatedMessageResponse;
import com.poly.booking.management.domain.message.reponse.PaymentMessageResponse;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.outbox.payload.PaymentEventPayload;
import com.poly.booking.management.domain.outbox.payload.RoomEventPayload;
import com.poly.booking.management.domain.outbox.payload.NotifiEventPayload;
import com.poly.domain.valueobject.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class BookingMessageDataMapper {

    public CustomerCreatedMessageResponse customerAvroToCustomerEntity(CustomerModelAvro customerModelAvro) {
        return CustomerCreatedMessageResponse.builder().customerId(customerModelAvro.getId()).firstName(customerModelAvro.getFirstName()).lastName(customerModelAvro.getLastName()).username(customerModelAvro.getUsername()).build();
    }

    public PaymentMessageResponse paymentResponseAvroToPayment(BookingPaymentResponseAvro bookingPaymentResponseAvro) {

        return PaymentMessageResponse.builder()
                .id(bookingPaymentResponseAvro.getId())
                .bookingId(bookingPaymentResponseAvro.getBookingId())
                .paymentId(bookingPaymentResponseAvro.getPaymentId())
                .price(bookingPaymentResponseAvro.getPrice())
                .createdAt(bookingPaymentResponseAvro.getCreatedAt())
                .customerId(bookingPaymentResponseAvro.getCustomerId())
                .sagaId(bookingPaymentResponseAvro.getSagaId())
                .paymentStatus(bookingPaymentResponseAvro.getPaymentStatus().equals(PaymentStatus.COMPLETED) ? com.poly.domain.valueobject.PaymentStatus.PAID : com.poly.domain.valueobject.PaymentStatus.FAILED)
                .failureMessages(bookingPaymentResponseAvro.getFailureMessages()).build();
    }

    public RoomMessageResponse bookingRoomAvroToRoom(BookingRoomResponseAvro bookingRoomResponseAvro) {
        return RoomMessageResponse.builder()
                .id(bookingRoomResponseAvro.getId())
                .bookingId(bookingRoomResponseAvro.getBookingId())
                .reason(bookingRoomResponseAvro.getReason())
                .roomResponseStatus(bookingRoomResponseAvro.getReservationStatus().equals("SUCCESS")? RoomStatus.BOOKED: RoomStatus.VACANT)
                .roomBookingStatus(RoomResponseStatus.SUCCESS)
                .sagaId(bookingRoomResponseAvro.getSagaId())
                .totalPrice(bookingRoomResponseAvro.getTotalPrice())
                .rooms(roomsAvroToRooms(bookingRoomResponseAvro.getRooms())).build();
    }

    private List<Room> roomsAvroToRooms(List<com.poly.booking.management.domain.kafka.model.Room> roomsAvro) {
        return roomsAvro.stream().map(roomAvro -> new Room(new RoomId(UUID.fromString(roomAvro.getId())), roomAvro.getRoomNumber(), Money.from(roomAvro.getBasePrice()), RoomStatus.valueOf(roomAvro.getStatus()))).toList();
    }

    private List<com.poly.booking.management.domain.kafka.model.Room> roomsToRoomsDepositAvro(List<RoomEventPayload> rooms) {
        return rooms.stream().map(room -> com.poly.booking.management.domain.kafka.model.Room.newBuilder()
                .setId(room.getRoomId())
                .setStatus(RoomStatus.VACANT.name())
                .build()).toList();
    }
    private List<com.poly.booking.management.domain.kafka.model.Room> roomsToRoomsCheckInAvro(List<RoomEventPayload> rooms) {
        return rooms.stream().map(room -> com.poly.booking.management.domain.kafka.model.Room.newBuilder()
                .setId(room.getRoomId())
                .setStatus(RoomStatus.CHECKED_IN.name())
                .build()).toList();
    }

    public BookingPaymentRequestAvro bookingPaymentEventToPaymentRequestAvroModel(String sagaId, PaymentEventPayload paymentEventPayload) {
        return BookingPaymentRequestAvro.newBuilder().setId(UUID.randomUUID().toString()).setSagaId(UUID.fromString(sagaId).toString()).setCustomerId(paymentEventPayload.getCustomerId()).setPaymentBookingStatus(PaymentBookingStatus.valueOf(paymentEventPayload.getPaymentBookingStatus())).setPrice(paymentEventPayload.getPrice()).setBookingId(paymentEventPayload.getBookingId()).setCreatedAt(Instant.from(paymentEventPayload.getCreatedAt())).build();
    }

    /**
     * Chuyển đổi BookingRoomEventPayload thành BookingRoomRequestAvro
     * <p>
     * Mục đích: Tạo Avro model để gửi yêu cầu đặt phòng đến room service
     * Sử dụng: Trong RoomRequestKafkaPublisher để gửi message đến Kafka
     *
     * @param sagaId               Saga ID để theo dõi quy trình
     * @param reservedEventPayload Thông tin phòng cần đặt
     * @return BookingRoomRequestAvro model
     */
    public BookingRoomRequestAvro bookingRoomEventToRoomRequestAvroModel(String sagaId, ReservedEventPayload reservedEventPayload) {
        return BookingRoomRequestAvro.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(sagaId))
                .setBookingId(reservedEventPayload.getBookingId())
                .setCreatedAt(Instant.now()).setProcessedAt(null)
                .setType("ROOM_RESERVATION_REQUEST")
                .setSagaStatus("STARTED")
                .setRooms(roomsToRoomsDepositAvro(reservedEventPayload.getRooms()))
                .setBookingStatus(BookingStatus.DEPOSITED.toString())
                .setPrice(reservedEventPayload.getPrice())
                .build();
    }

    public BookingRoomRequestAvro bookingRoomCheckInEventToRoomRequestAvroModel(String sagaId, ReservedEventPayload reservedEventPayload) {
        return BookingRoomRequestAvro.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(sagaId))
                .setBookingId(reservedEventPayload.getBookingId())
                .setCreatedAt(Instant.now()).setProcessedAt(null)
                .setType("ROOM_CHECKING_REQUEST")
                .setSagaStatus("STARTED")
                .setRooms(roomsToRoomsCheckInAvro(reservedEventPayload.getRooms()))
                .setBookingStatus(BookingStatus.CHECKED_IN.name())
                .setPrice(reservedEventPayload.getPrice())
                .build();
    }

    /**
     * Chuyển đổi BookingNotifiEventPayload thành NotificationModelAvro
     * <p>
     * Mục đích: Tạo Avro model để gửi thông báo xác nhận booking đến notification service
     * Sử dụng: Trong ConfirmedRequestKafkaPublisher để gửi message đến Kafka
     *
     * @param sagaId             Saga ID để theo dõi quy trình
     * @param notifiEventPayload Thông tin notification cần gửi
     * @return NotificationModelAvro model
     */
    public NotificationMessageAvro bookingNotificationEventToNotificationModelAvro(String sagaId, NotifiEventPayload notifiEventPayload) {
        return NotificationMessageAvro.newBuilder()
                .setId(notifiEventPayload.getId().toString())
                .setBookingId(notifiEventPayload.getBookingId().toString())
                .setCustomerId(notifiEventPayload.getCustomerId().toString())
                .setCustomerEmail(notifiEventPayload.getCustomerEmail())
                .setNotificationType(NotificationType.BOOKING_CONFIRMATION)
                .setMessageStatus(MessageStatus.PENDING)
                .build();
    }
}
