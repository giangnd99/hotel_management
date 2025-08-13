package com.poly.booking.management.messaging.mapper;

import com.poly.booking.management.domain.dto.message.CustomerCreatedMessageResponse;
import com.poly.booking.management.domain.dto.message.PaymentMessageResponse;
import com.poly.booking.management.domain.dto.message.RoomMessageResponse;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.kafka.model.*;
import com.poly.booking.management.domain.outbox.payload.PaymentEventPayload;
import com.poly.booking.management.domain.outbox.payload.RoomEventPayload;
import com.poly.booking.management.domain.outbox.payload.NotifiEventPayload;
import com.poly.domain.valueobject.*;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.booking.management.domain.kafka.model.NotificationStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class BookingMessageDataMapper {
    public CustomerCreatedMessageResponse customerAvroToCustomerEntity(CustomerModelAvro customerModelAvro) {
        return CustomerCreatedMessageResponse.builder()
                .id(customerModelAvro.getId())
                .firstName(customerModelAvro.getFirstName())
                .lastName(customerModelAvro.getLastName())
                .username(customerModelAvro.getUsername())
                .build();
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
                .paymentStatus(PaymentStatus.valueOf(bookingPaymentResponseAvro.getPaymentStatus().name()))
                .failureMessages(bookingPaymentResponseAvro.getFailureMessages())
                .build();
    }

    public RoomMessageResponse bookingRoomAvroToRoom(BookingRoomResponseAvro bookingRoomResponseAvro) {
        return RoomMessageResponse.builder()
                .id(bookingRoomResponseAvro.getId())
                .bookingId(bookingRoomResponseAvro.getBookingId())
                .reason(bookingRoomResponseAvro.getReason())
                .roomResponseStatus(RoomResponseStatus.valueOf(bookingRoomResponseAvro.getReservationStatus()))
                .sagaId(bookingRoomResponseAvro.getSagaId())
                .totalPrice(bookingRoomResponseAvro.getTotalPrice())
                .rooms(roomsAvroToRooms(bookingRoomResponseAvro.getRooms()))
                .build();
    }

    private List<Room> roomsAvroToRooms(List<com.poly.booking.management.domain.kafka.model.Room> roomsAvro) {
        return roomsAvro.stream().map(roomAvro ->
                new Room(new RoomId(UUID.fromString(roomAvro.getId())),
                        roomAvro.getRoomNumber(),
                        Money.from(roomAvro.getBasePrice()),
                        RoomStatus.valueOf(roomAvro.getStatus()))
        ).toList();
    }

    public BookingPaymentRequestAvro bookingPaymentEventToPaymentRequestAvroModel(String sagaId, PaymentEventPayload paymentEventPayload) {
        return BookingPaymentRequestAvro.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(UUID.fromString(sagaId).toString())
                .setCustomerId(paymentEventPayload.getCustomerId())
                .setPaymentBookingStatus(PaymentBookingStatus.valueOf(paymentEventPayload.getPaymentBookingStatus()))
                .setPrice(paymentEventPayload.getPrice())
                .setBookingId(paymentEventPayload.getBookingId())
                .setCreatedAt(Instant.from(paymentEventPayload.getCreatedAt()))
                .build();
    }

    /**
     * Chuyển đổi BookingRoomEventPayload thành BookingRoomRequestAvro
     * <p>
     * Mục đích: Tạo Avro model để gửi yêu cầu đặt phòng đến room service
     * Sử dụng: Trong RoomRequestKafkaPublisher để gửi message đến Kafka
     *
     * @param sagaId           Saga ID để theo dõi quy trình
     * @param roomEventPayload Thông tin phòng cần đặt
     * @return BookingRoomRequestAvro model
     */
    public BookingRoomRequestAvro bookingRoomEventToRoomRequestAvroModel(String sagaId, RoomEventPayload roomEventPayload) {
        return BookingRoomRequestAvro.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.fromString(sagaId))
                .setBookingId(roomEventPayload.getRoomId()) // Sử dụng roomId làm bookingId
                .setCreatedAt(Instant.now())
                .setProcessedAt(null)
                .setType("ROOM_RESERVATION_REQUEST")
                .setSagaStatus("STARTED")
                .setBookingStatus("PENDING")
                .setPrice(roomEventPayload.getBasePrice())
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
    public NotificationModelAvro bookingNotificationEventToNotificationModelAvro(String sagaId, NotifiEventPayload notifiEventPayload) {
        return NotificationModelAvro.newBuilder()
                .setId(notifiEventPayload.getId().toString())
                .setSagaId(sagaId)
                .setBookingId(notifiEventPayload.getBookingId().toString())
                .setCustomerId(notifiEventPayload.getCustomerId().toString())
                .setCheckInTime(notifiEventPayload.getCheckInTime().atZone(java.time.ZoneOffset.UTC).toInstant())
                .setNotificationStatus(NotificationStatus.valueOf(notifiEventPayload.getNotificationStatus()))
                .setBookingStatus(notifiEventPayload.getBookingStatus().toString())
                .setFailureMessages(new java.util.ArrayList<>()) // Empty list for success case
                .build();
    }
}
